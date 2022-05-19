package mips;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class TacMipsAdapter.
 */
public class TacMipsAdapter {

    /**
     * We adapt the TAC to make it feasible with MIPS
     *
     * @param lines the lines from tac code
     * @return the array list adapted
     */
    public static ArrayList<String> adaptTac(ArrayList<String> lines) {
        int[] high = findHighestRegiter(lines);
        int[] highestRegister = {high[0]};
        int[] highestLabel = {high[1]};

        Matcher matcher;
        Pattern operation = Pattern.compile("(t\\d+) := (t?-?\\d+) ([+\\-*/%&|]|==|!=|<|>|>=|<=) (t?-?\\d+)");

        Pattern not = Pattern.compile("(.*)(!t?-?\\d+)(.*)");
        Pattern boolIf = Pattern.compile("(.*if )(!?t?-?\\d+)( goto.*)");
        Pattern notEqual = Pattern.compile("(t\\d+)( := )!(t?\\d+)");
        Pattern compareIf = Pattern.compile("(.*if )(t?-?\\d+) (==|!=|<|>|>=|<=) (t?-?\\d+)( goto.*)");

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            // Not equal
            matcher = notEqual.matcher(line);
            if (matcher.matches()) {
                lines.remove(i);
                int label1 = ++highestLabel[0];
                int label2 = ++highestLabel[0];
                String arg1 = "t" + ++highestRegister[0];
                ArrayList<String> newLines = new ArrayList<>();

                newLines.add(matcher.replaceFirst("if $3 != 0 goto L"+label1));
                newLines.add(matcher.replaceFirst(arg1+" =: 1"));
                newLines.add(matcher.replaceFirst("goto L"+label2));
                newLines.add(matcher.replaceFirst("L"+label1+": "+arg1+" =: 0"));
                newLines.add("L"+label2+":");
                lines.addAll(i, newLines);
                line = lines.get(i);
            }

            // Boolean if
            matcher = boolIf.matcher(line);
            if (matcher.matches()) {
                lines.remove(i);
                String value = matcher.group(2);
                String symbol;
                if (value.charAt(0) == '!') {
                    symbol = "==";
                    value = value.substring(1);
                } else {
                    symbol = "!=";
                }
                lines.add(i, matcher.replaceFirst("$1"+value+" "+symbol+" 0$3"));
                line = lines.get(i);
            }

            // Compare if
            matcher = compareIf.matcher(line);
            if (matcher.matches()) {
                String arg1 = matcher.group(2);
                String arg2 = matcher.group(4);
                if (!arg1.contains("t") || !arg2.contains("t")) {
                    ArrayList<String> newLines;
                    newLines = doWithRegisters(matcher, highestRegister);
                    lines.remove(i);
                    lines.addAll(i, newLines);
                    i += newLines.size() - 1;
                }
            }

            matcher = operation.matcher(line);
            // Per cada operació trobada
            while (matcher.find()) {
                String arg1 = matcher.group(2);
                String arg2 = matcher.group(4);
                String symbol = matcher.group(3);

                // Logic operations
                if (isLogicOperation(symbol)) {
                    ArrayList<String> newLines = translateBooleanOperation(matcher, highestLabel, highestRegister);
                    lines.remove(i);
                    lines.addAll(i, newLines);
                    i--;
                    continue;
                }

                // Format number operations
                if (!arg1.contains("t")) {
                    ArrayList<String> newLines;
                    // Two literals
                    if (symbol.equals("*") || symbol.equals("/") || symbol.equals("%") || !arg2.contains("t")) {
                        newLines = doWithRegisters(matcher, highestRegister);
                    } else {
                        newLines = swapArguments(matcher);
                    }
                    lines.remove(i);
                    lines.addAll(i, newLines);
                    i += newLines.size() - 1;
                }
            }
        }
        return lines;
    }

    private static boolean isLogicOperation(String symbol) {
        return symbol.equals("&") || symbol.equals("|") || symbol.contains("=") || symbol.contains("<") || symbol.contains(">");
    }

    private static ArrayList<String> translateBooleanOperation(Matcher line, int[] highestLabel, int[] highestRegister) {
        String symbol = line.group(3);
        ArrayList<String> results = new ArrayList<>();
        String assign1 = line.replaceFirst("$1 := 1");
        String assign0 = line.replaceFirst("$1 := 0");
        int label1 = ++highestLabel[0];
        int label2 = ++highestLabel[0];
        switch (symbol) {
            case "&":
                results.add(line.replaceFirst("if $2 == 0 goto L" + label1));
                results.add(line.replaceFirst("if $4 == 0 goto L" + label1));
                results.add(assign1);
                results.add("goto L"+label2);
                results.add("L"+label1+": "+assign0);
                results.add("L"+label2+":");
                break;

            case "|":
                results.add(line.replaceFirst("if $2 != 0 goto L" + label1));
                results.add(line.replaceFirst("if $4 == 0 goto L" + label1));
                results.add(assign1);
                results.add("goto L"+label2);
                results.add("L"+label1+": "+assign0);
                results.add("L"+label2+":");
                break;

            default:
                results.add(line.replaceFirst("if $2 $3 $4 goto L" + label1));
                results.add(assign0);
                results.add("goto L"+label2);
                results.add("L"+label1+": "+assign1);
                results.add("L"+label2+":");
                break;
        }
        return results;
    }


    private static ArrayList<String> doWithRegisters(Matcher line, int[] maxRegisters) {
        ArrayList<String> results = new ArrayList<>();
        String arg1 = line.group(2);
        String arg2 = line.group(4);
        String symbol = line.group(3);
        if (!line.group(2).contains("t")) {
            arg1 = "t" + ++maxRegisters[0];
            results.add(arg1 + line.replaceFirst(" := $2"));
        }
        if (!line.group(4).contains("t") && !(symbol.equals("+") || symbol.equals("-"))) {  // Si estem en + o - no cal posar el segon a registre també, estalviem registres
            arg2 = "t" + ++maxRegisters[0];
            results.add(arg2 + line.replaceFirst(" := $4"));
        }
        if (symbol.contains("=") || symbol.contains("<") || symbol.contains(">")) {
            results.add(line.group(1) + arg1 + " " + line.group(3) + " " + arg2 + line.group(5));
        } else {
            results.add(line.group(1) + " := " + arg1 + " " + line.group(3) + " " + arg2);
        }
        return results;
    }

    private static ArrayList<String> swapArguments(Matcher line) {
        String result = "";
        switch (line.group(3)) {
            case "+":
                result = line.replaceFirst("$1 := $4 $3 $2");
                break;
            case "-":
                result = line.replaceFirst("$1 := -$4 + $2");
                break;
        }
        if (result.equals("")) throw new InvalidTacException();
        return new ArrayList<>(List.of(result));
    }

    private static int[] findHighestRegiter(ArrayList<String> lines) {
        int[] highest = {0, 0};
        Matcher matcher;
        Pattern registerPattern = Pattern.compile("t(\\d+)");
        Pattern label = Pattern.compile("L(\\d+)");

        for (String line : lines) {
            matcher = registerPattern.matcher(line);
            while (matcher.find()) {
                int register = Integer.parseInt(matcher.group(1));
                if (register > highest[0]) highest[0] = register;
            }
            matcher = label.matcher(line);
            while (matcher.find()) {
                int labelNum = Integer.parseInt(matcher.group(1));
                if (labelNum > highest[1]) highest[1] = labelNum;
            }
        }
        return highest;
    }
}
