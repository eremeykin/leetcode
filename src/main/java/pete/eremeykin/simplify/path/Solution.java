package pete.eremeykin.simplify.path;
// https://leetcode.com/problems/simplify-path/submissions/
import java.util.LinkedList;

public class Solution {
    private static final String LEVEL_UP_DIRECTORY = "..";
    private static final String SLASH = "/";
    private static final String CURRENT_LEVEL_DIRECTORY = ".";
    private final LinkedList<String> tokens = new LinkedList<>();

    public String simplifyPath(String path) {
        for (String token : path.split("\\/+")) {
            if (token.equals(LEVEL_UP_DIRECTORY) && !tokens.isEmpty()) {
                if (!tokens.peekLast().isEmpty()) tokens.removeLast();
            } else if (!token.equals(CURRENT_LEVEL_DIRECTORY) && !token.equals(SLASH)) {
                tokens.add(token);
            }
        }
        String join = String.join(SLASH, tokens);
        return join.startsWith(SLASH) ? join : SLASH + join;
    }
}