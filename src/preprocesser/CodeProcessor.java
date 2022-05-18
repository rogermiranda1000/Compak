package preprocesser;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.NoSuchElementException;

/**
 * Class CodeProcessor.
 */
public class CodeProcessor implements LineRequest {
    private static final Pattern comment = Pattern.compile("^([^\"/]|(?<=/)?/(?>/)?)*(?:\"(?:[^\"]|(?:(?<=\\\\)\"))*\"[^\"/]*)*//"); // detect comments outside text
    private final FileManager fm;
    private final ElseToIf elseToIf;

    /**
     * Constructor for CodeProcessor.
     *
     * @param path path file
     * @throws FileNotFoundException the file not found exception
     */
    public CodeProcessor(String path) throws FileNotFoundException {
        this.fm = new FileManager(path);
        this.elseToIf = new ElseToIf();
    }

    /**
     * Gets next line from the file.
     *
     * @return next line string
     * @throws NoSuchElementException the no such element exception
     */
    public String getNextLine() throws NoSuchElementException {
        String next = this.elseToIf.processLine(this.fm.readNextLine());
        Matcher match = CodeProcessor.comment.matcher(next);
        if (match.find()) next = next.substring(0, match.end()-2);
        return next;
    }
}
