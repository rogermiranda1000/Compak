package mips;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class RegisterManager. Consists on mange the control and optimizer of registers for mips.
 */
public class RegisterManager {
    /**
     * Gets usage of registers.
     *
     * @param lines                  lines
     * @param biggestVirtualRegister virtual register
     * @return the usage of registers
     */
    public static ArrayList<Integer> getUsageOfRegisters(ArrayList<String> lines, int[] biggestVirtualRegister) {
        HashMap<Integer, ArrayList<Integer>> registersEnding = new HashMap<>();

        int lineWeAreAt = 1;

        for(String line: lines) {

            String[] tokens = line.split(" ");

            for(String token : tokens) {
                if(token.matches("^t\\d+$")) {
                    int registerNumber = Integer.parseInt(token.substring(1));
                    //Si no esta directament mapejat el agreugem a la taula
                    if(!registersEnding.containsKey(registerNumber)) registersEnding.put(registerNumber, new ArrayList<>());

                    registersEnding.get(registerNumber).add(lineWeAreAt);

                    if(biggestVirtualRegister[0] < registerNumber) biggestVirtualRegister[0] = registerNumber;
                }

            }
            lineWeAreAt++;
        }

        ArrayList<Integer> endsUsageAt = new ArrayList<>();
        for(Integer idRegister : registersEnding.keySet()) {
            Collections.sort(registersEnding.get(idRegister));

            endsUsageAt.add(registersEnding.get(idRegister).get(registersEnding.get(idRegister).size()-1));
        }
        return endsUsageAt;
    }

    private static void addRegistersToNotFree(ArrayList<String> lines) {
        ArrayList<Integer> activeLoops = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> loopRegisters = new HashMap<>();
        Pattern loopStart = Pattern.compile("^L(\\d+): if");
        Pattern register = Pattern.compile("t(\\d+)");
        Pattern loopEnd = Pattern.compile("^goto L(\\d+)$");
        Matcher matcher;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // Detectar nous bucles
            matcher = loopStart.matcher(line);
            if (matcher.find()) {
                int label = Integer.parseInt(matcher.group(1));
                // Afegim nou bucle
                activeLoops.add(label);
                loopRegisters.put(label, new ArrayList<>());
            }
            // Afegir registres als bucles
            matcher = register.matcher(line);
            while (matcher.find()) {
                int registerN = Integer.parseInt(matcher.group(1));
                for (Integer loop : activeLoops) {
                    ArrayList<Integer> savedRegisters = loopRegisters.get(loop);
                    if (!savedRegisters.contains(registerN)) {
                        savedRegisters.add(registerN);
                    }
                }
            }
            // Tancar loops
            matcher = loopEnd.matcher(line.trim());
            if (matcher.matches()) {
                int label = Integer.parseInt(matcher.group(1));
                if (activeLoops.contains(label)) {
                    activeLoops.remove((Integer) label);

                    // Add new line
                    ArrayList<String> registers = new ArrayList<>();
                    Collections.sort(loopRegisters.get(label));
                    for (Integer registerNum : loopRegisters.get(label)) {
                        registers.add("t"+registerNum);
                    }
                    String newLine = "# remember: " + String.join(" ", registers);
                    lines.add(i+1, newLine);
                }
            }
        }
    }

    /**
     * Algorithm K coloring graph register generator array of strings.
     *
     * @param lines        lines
     * @param numRegisters the number of registers
     * @return array of strings
     * @throws NoMoreRegistersException the no more registers exception
     */
    public static String[] kColoringGraphRegisterGenerator(ArrayList<String> lines, int numRegisters) throws NoMoreRegistersException {
        addRegistersToNotFree(lines);

        int[] biggestVirtualRegisterArray = new int[1];
        biggestVirtualRegisterArray[0] = -1;
        ArrayList<Integer> endingUsageOfRegisters = getUsageOfRegisters(lines, biggestVirtualRegisterArray);
        int biggestVirtualRegister = biggestVirtualRegisterArray[0] +1 ;

        HashMap<Integer, ArrayList<Integer>> inverseEndingUsageOfRegisters = new HashMap<>();
        for(int i=0; i < endingUsageOfRegisters.size(); i++) {
            if(!inverseEndingUsageOfRegisters.containsKey(endingUsageOfRegisters.get(i))) inverseEndingUsageOfRegisters.put(endingUsageOfRegisters.get(i), new ArrayList<>());
            inverseEndingUsageOfRegisters.get(endingUsageOfRegisters.get(i)).add(i);
        }


        boolean[] freeRegisters = new boolean[numRegisters];

        int[] assignation = new int[biggestVirtualRegister];

        for(int i=0; i < biggestVirtualRegister; i++) assignation[i] = -1;

        int countLines = 0;
        for(String line: lines) {
            String[] tokens = line.split(" ");
            for (String token : tokens) {
                if (token.matches("^t\\d+$")) {
                    int idVirtualRegister = Integer.parseInt(token.substring(1));
                    int positionFreeRegister;
                    if(assignation[idVirtualRegister] == -1) {
                        if ((positionFreeRegister = existsFreeRegister(freeRegisters)) != -1) {
                            freeRegisters[positionFreeRegister] = true;
                            assignation[idVirtualRegister] = positionFreeRegister;

                        } else throw new NoMoreRegistersException("No ens queden mes registres :(\n La linea a la que falla es: " + countLines);
                    }
                }
            }

            countLines++;
            if(!inverseEndingUsageOfRegisters.containsKey(countLines)) continue;
            for(Integer freeVirtualRegister : inverseEndingUsageOfRegisters.get(countLines)) {
                freeRegisters[assignation[freeVirtualRegister]] = false;
            }
        }
        int countRemembers = 0;
        for(String line : lines){
            if(line.startsWith("# remember:")) {
                countRemembers++;
            }
        }


        String[] newLines = new String[lines.size() - countRemembers];
        int countNewLines = 0;
        for(String line: lines) {

            String[] tokens = line.split(" ");
            StringBuilder newLine = new StringBuilder();

            for (String token : tokens) {

                if (token.matches("^t\\d+$")) {
                    int idVirtualRegister = Integer.parseInt(token.substring(1));
                    newLine.append("t" + assignation[idVirtualRegister] + " ");

                }else newLine.append(token + " ");
            }
            if(!newLine.toString().startsWith("# remember:")) {
                newLines[countNewLines] = newLine.toString().trim();
                countNewLines++;
            }
        }

        return newLines;

    }

    private static int existsFreeRegister(boolean[] freeRegisters) {
        int count = 0;
        for(boolean reg : freeRegisters){
            if(!reg) return count;
            count++;
        }
        return -1;
    }


}
