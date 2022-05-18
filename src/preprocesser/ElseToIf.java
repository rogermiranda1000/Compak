package preprocesser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElseToIf {
    private static final Pattern ifCondition = Pattern.compile("if\\s*\\((.*?)\\)\\s*\\{((.*)\\}\\s*(else))");
    private final StringBuilder previousLines;

    public ElseToIf() {
        this.previousLines = new StringBuilder();
    }

    public String processLine(String line) {
        String r = line;
        Matcher match = ifCondition.matcher(this.previousLines + r);
        while (match.find()) {
            // get else position (to remove later)
            int elsePos = match.start(4) - this.previousLines.length();

            int context;
            do {
                // does it have the same number of '{' and '}'?
                context = 0;
                String check = match.group(3);
                for (int i = 0; i < check.length(); i++) {
                    if (check.charAt(i) == '{') context++;
                    else if (check.charAt(i) == '}') context--;
                }

                if (context != 0) match = ifCondition.matcher(match.group(2));
            } while (context != 0 && match.find());

            if (context != 0) break;

            r = r.substring(0, elsePos) + "if (¡(" + match.group(1) + "))" + r.substring(elsePos + "else".length());
            match = ifCondition.matcher(this.previousLines + r);
        }
        this.previousLines.append(r);
        return r;
    }
}
