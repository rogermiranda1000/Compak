package mips;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TacMipsAdapter {
    private static final Pattern registerPattern = Pattern.compile("t(\\d+)");

    public static ArrayList<String> adaptTac(ArrayList<String> lines) {
        int[] high = findHighestRegiter(lines);
        int[] highestRegister = {high[0]};
        int[] highestLabel = {high[1]};

        Matcher matcher;
        Pattern operation = Pattern.compile("(t\\d+) := (t?-?\\d+) ([+\\-*/%&|]) (t?-?\\d+)");
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            matcher = operation.matcher(line);
            // Per cada operació trobada
            while (matcher.find()) {
                String arg1 = matcher.group(2);
                String arg2 = matcher.group(4);
                String symbol = matcher.group(3);

                // Logic operations
                if (isLogicOperation(symbol)) {
                    ArrayList<String> newLines = translateBooleanOperation(matcher, highestLabel);
                    lines.remove(i);
                    lines.addAll(i, newLines);
                    i += newLines.size() - 1;
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
        return symbol.equals("&") || symbol.equals("|");
    }

    private static ArrayList<String> translateBooleanOperation(Matcher line, int[] highestLabel) {
        String symbol = line.group(3);
        ArrayList<String> results = new ArrayList<>();
        String assign = line.replaceFirst("$1 := 1");
        int label1 = ++highestLabel[0];
        switch (symbol) {
            case "&":
                results.add(line.replaceFirst("if := $2 != 0 goto L" + label1));
                results.add(line.replaceFirst("if := $4 != 0 goto L" + label1));
                results.add(assign);
                results.add("L"+label1+":");
                break;

            case "|":
                int label2 = ++highestLabel[0];
                results.add(line.replaceFirst("if := $2 != 0 goto L" + label1));
                results.add(line.replaceFirst("if := $4 == 0 goto L" + label2));
                results.add("L"+label1+": " + assign);
                results.add("L"+label2+":");
                break;

            default:
                throw new InvalidTacException();
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
        results.add(line.group(1) + " := " + arg1 + " " + line.group(3) + " " + arg2);
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
        int highest[] = {0, 0};
        Matcher matcher;
        for (String line : lines) {
            matcher = registerPattern.matcher(line);
            while (matcher.find()) {
                int register = Integer.parseInt(matcher.group(1));
                if (register > highest[0]) highest[0] = register;
            }
        }
        return highest;
    }
}
