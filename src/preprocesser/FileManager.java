package preprocesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class FileManager. It only consists on read lines of the file to preprocess.
 */
public class FileManager {
    private final Scanner file;

    /**
     * Constructor for FileManager.
     *
     * @param path the path
     * @throws FileNotFoundException the file not found exception
     */
    public FileManager(String path) throws FileNotFoundException {
        this.file = new Scanner(new File(path));
    }

    /**
     * Read next line from file.
     *
     * @return next line string
     * @throws NoSuchElementException the no such element exception
     */
    public String readNextLine() throws NoSuchElementException {
        return this.file.nextLine();
    }
}


