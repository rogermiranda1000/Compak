package preprocesser;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeProcessor implements LineRequest {
    private static final Pattern comment = Pattern.compile("^([^\"/]|(?<=/)?/(?>/)?)*(?:\"(?:[^\"]|(?:(?<=\\\\)\"))*\"[^\"/]*)*//"); // detect comments outside text
    private final FileManager fm;

    public CodeProcessor(String path) throws FileNotFoundException {
        this.fm = new FileManager(path);
    }

    public String getNextLine() {
        String next = this.fm.readNextLine();
        Matcher match = CodeProcessor.comment.matcher(next);
        if (match.find()) next = next.substring(0, match.end()-2);
        return next;
    }

    public static void main(String[] args) throws FileNotFoundException {
        CodeProcessor cp = new CodeProcessor("test.sus");
        System.out.println(cp.getNextLine());
        System.out.println(cp.getNextLine());
        System.out.println(cp.getNextLine());
    }
}
