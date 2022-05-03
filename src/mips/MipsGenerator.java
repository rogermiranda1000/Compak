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
        } catch (NoMoreRegistersException e) {
            throw new RuntimeException(e);
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

    public static void createMIPS(ArrayList<String> lines, FileWriter writer) throws IOException, NoMoreRegistersException {
        ArrayList<String> out = new ArrayList<>();
        out.add(".text\n");

        RegisterManager.kColoringGraphRegisterGenerator(lines, 8);

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
            expr = mipsAssign(tokens);
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
                case "/":
                    expr = mipsDiv(tokens);
                    break;
                case "%":
                    expr = mipsMod(tokens);
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
        String operation = anyNumbers(tokens[2], tokens[4]) ? "addi" : "add";
        return operation + " " + formatArg(tokens[0]) + ", " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]);
    }

    private static String mipsSub(String[] tokens) {
        String operation = anyNumbers(tokens[2], tokens[4]) ? "subi" : "sub";
        return operation + " " + formatArg(tokens[0]) + ", " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]);
    }

    private static String moveLow(String token) {
        return "mflo " + formatArg(token);
    }

    private static String moveHigh(String token) {
        return "mfhi " + formatArg(token);
    }

    // Per multiplicacions i divisions si es vol der variable * numero cal posar numero a un registre abans
    private static String mipsMult(String[] tokens) {
        if (anyNumbers(tokens[2], tokens[4])) throw new InvalidTacException();
        String operation = "mult";
        return operation + " " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]) + "\n" +
                moveLow(tokens[0]);
    }

    private static String mipsDiv(String[] tokens) {
        if (anyNumbers(tokens[2], tokens[4])) throw new InvalidTacException();
        String operation = "div";
        return operation + " " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]) + "\n" +
                moveLow(tokens[0]);
    }

    private static String mipsMod(String[] tokens) {
        if (anyNumbers(tokens[2], tokens[4])) throw new InvalidTacException();
        String operation = "div";
        return operation + " " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]) + "\n" +
                moveHigh(tokens[0]);
    }

    private static String mipsAssign(String[] tokens) {
        String operation = isNumber(tokens[2]) ? "li" : "move";
        return operation + " " + formatArg(tokens[0]) + ", " + formatArg(tokens[2]);
    }

    private static boolean anyNumbers(String token1, String token2) {
        return isNumber(token1) || isNumber(token2);
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
