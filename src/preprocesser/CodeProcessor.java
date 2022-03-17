package preprocesser;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

public class CodeProcessor implements LineRequest {
    private final FileManager fm;

    public CodeProcessor(String path) throws FileNotFoundException {
        this.fm = new FileManager(path);
    }

    public String getNextLine() throws NoSuchElementException {
        return this.fm.readNextLine().replaceAll("//.*$", ""); // TODO si añadimos string hay que comprobar que no haya '//' en literal
    }
}
