package mips;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public interface MipsConverter {
    /**
     * Create mips file with all data processed.
     *
     * @param inputFile  the input file
     * @param outputFile the output file
     */
    public void generateMipsFromFile(String inputFile, String outputFile);
}
