package mips;

import entities.TokenDataPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RegisterManager {
    private static final int lengthRegisters = 10;
    private Register[] registers = new Register[lengthRegisters];

    private RegisterManager() {
        for(int i=0; i < lengthRegisters; i++) {
            registers[i] = new Register(-1);
        }
    }

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

    public static void kColoringGraphRegisterGenerator(ArrayList<String> lines, int numRegisters) throws NoMoreRegistersException {
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
        String[] newLines = new String[lines.size()];
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
            newLines[countNewLines] = newLine.toString().trim();
            countNewLines++;
        }


        System.out.println("TEST");
    }

    private static int existsFreeRegister(boolean[] freeRegisters) {
        int count = 0;
        for(boolean reg : freeRegisters){
            if(!reg) return count;
            count++;
        }
        return -1;
    }

    private void checkIfWeCanFreeRegisters(int actualPosition) {
        for(int i=0; i < lengthRegisters; i++) {
            if(registers[i].getPositionThatEnds() < actualPosition) registers[i].setStatus(false);
        }
    }

    private int lookForFreeRegister() throws NoMoreRegistersException {
        for(int i=0; i < lengthRegisters; i++) {
            if(this.registers[i].getStatus()) return i;
        }
        throw new NoMoreRegistersException("ERROR: No more registers available");
    }

    private void freeRegister(int id) {
        registers[id].setStatus(false);
    }
}
