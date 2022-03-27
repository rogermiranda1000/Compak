package preprocesser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FileManager {
    private final Scanner file;

    public FileManager(String path) throws FileNotFoundException {
        this.file = new Scanner(new File(path));
    }

    public String readNextLine() throws NoSuchElementException {
        return this.file.nextLine();
    }
}
