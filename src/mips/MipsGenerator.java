package mips;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class Mips Generator. Generate each line in mips language processing all tac code given.
 */
public class MipsGenerator implements MipsConverter {
    private static final String INDENT = "    ";

    /**
     * Constructor for MipsGenerator.
     */
    public MipsGenerator() {

    }

    /**
     * Create mips file with all data processed.
     *
     * @param inputFile  the input file
     * @param outputFile the output file
     */
    @Override
    public void generateMipsFromFile(String inputFile, String outputFile) {
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

    private void createMIPS(ArrayList<String> lines, FileWriter writer) throws IOException, NoMoreRegistersException {
        ArrayList<String> out = new ArrayList<>();
        out.add(".text\n");
        out.add("j $main\n");

        TacMipsAdapter.adaptTac(lines); // Adaptem el TAC perquè sigui factible amb MIPS
        String[] newLines = RegisterManager.kColoringGraphRegisterGenerator(lines, 10);

        for (String line : newLines) {
            String[] tokens = line.split(" ");
            String expression = generateMIPSExpression(tokens);
            out.add(expression);
        }

        // Write to file
        for (String line : out) {
            writer.write(line + "\n");
        }
    }

    private String generateMIPSExpression(String[] tokens) {
        String expr = INDENT, label;
        label = checkLabel(tokens); // Check if label was present in line
        if (!label.isEmpty()) tokens = cutFrom(1, tokens);

        if (tokens.length == 0) {
            // Empty line
        } else if (tokens.length == 1) {
            if (tokens[0].equals("BeginFunc")) {
                expr += mipsStartFunction(tokens);
            } else if (tokens[0].equals("EndFunc")) {
                expr += mipsReturnFunction(tokens);
            }
        } else if (tokens.length == 2) {
            if (tokens[0].equals("Call")) {
                expr += mipsCallFunction(tokens);
            } else {
                expr += mipsGoto(tokens);
            }
        } else if (tokens.length == 3) {
            expr += mipsAssign(tokens);
        } else if (tokens.length == 5){
            switch (tokens[3]) {
                case "+":
                    expr += mipsSum(tokens);
                    break;
                case "-":
                    expr += mipsSub(tokens);
                    break;
                case "*":
                    expr += mipsMult(tokens);
                    break;
                case "/":
                    expr += mipsDiv(tokens);
                    break;
                case "%":
                    expr += mipsMod(tokens);
                    break;
                default:
                    expr += null;
            }
        } else if (tokens.length > 5) {
            expr += mipsIf(tokens);
        } else {
            expr += "ERROR";
        }
        return label + expr;
    }

    private ArrayList<String> getTACLines(String file) {
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

    private String mipsX(String[] tokens) {
        return "";
    }

    private String mipsStartFunction(String[] tokens) {
        return "# function call";
    }

    private String mipsCallFunction(String[] tokens) {
        return "jal $" + tokens[1];
    }

    private String mipsReturnFunction(String[] tokens) {
        return "jr $ra";
    }

    private String mipsGoto(String[] tokens) {
        return "j " + "$" + tokens[1];
    }

    private String mipsIf(String[] tokens) {
        String result = "ERROR";
        String command = switch (tokens[2]) {
            case "==" -> "beq";
            case "!=" -> "bne";
            case ">" -> "bgt";
            case ">=" -> "bge";
            case "<" -> "blt";
            case "<=" -> "ble";
            default -> "ERROR";
        };
        return command + " " + formatArg(tokens[1]) + ", " + formatArg(tokens[3]) + ", " + "$" + tokens[5];
    }

    private String checkLabel(String[] tokens) {
        boolean isAlone = tokens.length == 1;
        return tokens[0].matches("[\\d\\w]+:")  ? "$"+tokens[0]+(isAlone ? "" : "\n") : "";
    }

    // No podem rebre operacions entre dos literals
    // Literal ha d'anar després de variable
    private String mipsSum(String[] tokens) {
        String operation = anyNumbers(tokens[2], tokens[4]) ? "addi" : "add";
        return operation + " " + formatArg(tokens[0]) + ", " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]);
    }

    private String mipsSub(String[] tokens) {
        String operation = anyNumbers(tokens[2], tokens[4]) ? "subi" : "sub";
        return operation + " " + formatArg(tokens[0]) + ", " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]);
    }

    private String moveLow(String token) {
        return "mflo " + formatArg(token);
    }

    private String moveHigh(String token) {
        return "mfhi " + formatArg(token);
    }

    // Per multiplicacions i divisions si es vol der variable * numero cal posar numero a un registre abans
    private String mipsMult(String[] tokens) {
        if (anyNumbers(tokens[2], tokens[4])) throw new InvalidTacException();
        String operation = "mult";
        return operation + " " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]) + "\n" + INDENT +
                moveLow(tokens[0]);
    }

    private String mipsDiv(String[] tokens) {
        if (anyNumbers(tokens[2], tokens[4])) throw new InvalidTacException();
        String operation = "div";
        return operation + " " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]) + "\n" + INDENT +
                moveLow(tokens[0]);
    }

    private String mipsMod(String[] tokens) {
        if (anyNumbers(tokens[2], tokens[4])) throw new InvalidTacException();
        String operation = "div";
        return operation + " " + formatArg(tokens[2]) + ", " + formatArg(tokens[4]) + "\n" + INDENT +
                moveHigh(tokens[0]);
    }

    private String mipsAssign(String[] tokens) {
        String operation = isNumber(tokens[2]) ? "li" : "move";
        return operation + " " + formatArg(tokens[0]) + ", " + formatArg(tokens[2]);
    }

    private boolean anyNumbers(String token1, String token2) {
        return isNumber(token1) || isNumber(token2);
    }

    private boolean isNumber(String input) {
        return input.matches("\\d+");
    }

    private String formatArg(String argument) {
        if (argument.matches("-?t\\d")) {
            return "$" + argument;
        }
        return(argument);
    }

    private String[] cutFrom(int index, String[] array) {
        return Arrays.copyOfRange(array, index, array.length);
    }
}
