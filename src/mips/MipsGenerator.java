package mips;

import java.io.*;
import java.util.ArrayList;

public class MipsGenerator {
    public static void main(String[] args) {
        generateMipsFromFile("tac.txt", "mips.asm");
    }

    public static void generateMipsFromFile(String inputFile, String outputFile) {
        ArrayList<String> lines = getTACLines(inputFile);
        try {
            FileWriter myWriter = new FileWriter(outputFile);
            createMIPS(lines, myWriter);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getTACLines(String file) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the input file.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void createMIPS(ArrayList<String> lines, FileWriter writer) throws IOException {
        ArrayList<String> out = new ArrayList<>();
        out.add(".text\n");
        for (String line : lines) {
            String[] tokens = line.split(" ");
            String expression = generateMIPSExpression(tokens);
            out.add(expression);
        }

        // Write to file
        for (String line : out) {
            writer.write(line + "\n");
        }
    }

    public static String generateMIPSExpression(String[] tokens) {
        String expr;
        if (tokens.length <= 3) {
            expr = "li " + formatArg(tokens[0]) + ", " + formatArg(tokens[2]);
        } else {
            switch (tokens[3]) {
                case "+":
                    expr = mipsSum(tokens);
                    break;
                case "-":
                    expr = mipsSub(tokens);
                    break;
                case "*":
                    expr = mipsMult(tokens);
                    break;
                default:
                    expr = null;
            }
        }
        return expr;
    }
    // No podem rebre operacions entre dos literals
    // Literal ha d'anar després de variable
    private static String mipsSum(String[] tokens) {
        String operation = isNumber(tokens[2]) || isNumber(tokens[4]) ? "addi" : "add";
        return operation + " " + formatArg(tokens[0]) + ", " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]);
    }

    private static String mipsSub(String[] tokens) {
        String operation = isNumber(tokens[2]) || isNumber(tokens[4]) ? "subi" : "sub";
        return operation + " " + formatArg(tokens[0]) + ", " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]);
    }

    private static String mipsMult(String[] tokens) {
        String operation = isNumber(tokens[2]) || isNumber(tokens[4]) ? "mult???" : "mult";
        return operation + " " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]) + "\n" +
                "mflo " + formatArg(tokens[0]);
    }

    private static boolean isNumber(String input) {
        return input.matches("\\d+");
    }

    private static String formatArg(String argument) {
        if (argument.charAt(0) == 't') {
            return "$" + argument;
        }
        return(argument);
    }
}
