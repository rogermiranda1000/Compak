package intermediateCode;

import entities.ThreeAddressLine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The interface Tac Converter.
 */
public interface TacConverter {
    /**
     * Function that generates all TAC lines travelling on AST and printing each line.
     *
     * @param data               array with all TAC lines
     * @param out                file's out
     * @throws IOException       the io exception
     */
    public void process(ArrayList<ThreeAddressLine> data, File out) throws IOException;
}
