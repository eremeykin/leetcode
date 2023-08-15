package pete.eremeykin.simplify.path;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Solution {
    private final LinkedList<Token> tokens = new LinkedList<>();

    public String simplifyPath(String path) {
        new Parser(path).forEach(token -> token.apply(tokens));
        return SignalToken.SLASH.sign + tokens.stream()
                .map(Token::canonicalString)
                .collect(Collectors.joining(SignalToken.SLASH.sign));
    }

    static class Parser implements Iterable<Token> {
        private final String string;

        public Parser(String string) {
            this.string = string;
        }

        @Override
        public Iterator<Token> iterator() {
            return new Iterator<>() {
                private int pos = 0;

                @Override
                public boolean hasNext() {
                    return pos < string.length();
                }

                @Override
                public Token next() {
                    StringBuilder tokenBuilder = new StringBuilder();
                    int start = pos;
                    SignalToken signalToken;
                    while ((signalToken = readSignalToken()) == null && pos < string.length()) {
                        tokenBuilder.append(string.charAt(pos++));
                    }
                    if (tokenBuilder.isEmpty()) return signalToken;
                    pos = start + tokenBuilder.length();
                    return new FileNameToken(tokenBuilder.toString());
                }

                private SignalToken readSignalToken() {
                    for (SignalToken signalToken : SignalToken.values()) {
                        int newPos = signalToken.eagerlyReadFromString(string, pos);
                        if (newPos != pos) {
                            pos = newPos;
                            return signalToken;
                        }
                    }
                    return null;
                }
            };
        }
    }


    interface Token {
        String canonicalString();

        void apply(LinkedList<Token> tokens);
    }

    static class FileNameToken implements Token {
        private final String name;

        public FileNameToken(String name) {
            this.name = name;
        }

        @Override
        public String canonicalString() {
            return name;
        }

        @Override
        public void apply(LinkedList<Token> tokens) {
            tokens.add(this);
        }
    }

    enum SignalToken implements Token {
        SLASH("/") {
            @Override
            public int eagerlyReadFromString(String string, int pos) {
                while (pos < string.length() && string.charAt(pos) == '/') {
                    pos++;
                }
                return pos;
            }
        },
        CURRENT_DIRECTORY(".") {
            @Override
            public int eagerlyReadFromString(String string, int pos) {
                int start = pos;
                char prev = pos - 1 > 0 ? string.charAt(pos - 1) : '/';
                char first = pos < string.length() ? string.charAt(pos++) : '*';
                char last = pos < string.length() ? string.charAt(pos) : '/';
                return prev == '/' && first == '.' && last == '/' ? pos : start;
            }
        },
        UP_LEVEL_DIRECTORY("..") {
            @Override
            public void apply(LinkedList<Token> tokens) {
                if (!tokens.isEmpty()) tokens.removeLast();
            }

            @Override
            public int eagerlyReadFromString(String string, int pos) {
                int start = pos;
                char prev = pos - 1 > 0 ? string.charAt(pos - 1) : '/';
                char first = pos < string.length() ? string.charAt(pos++) : '*';
                char second = pos < string.length() ? string.charAt(pos++) : '*';
                char last = pos < string.length() ? string.charAt(pos) : '/';
                return prev == '/' && first == '.' && second == '.' && last == '/' ? pos : start;
            }
        };

        protected final String sign;

        SignalToken(String sign) {
            this.sign = sign;
        }

        @Override
        public String canonicalString() {
            return sign;
        }

        @Override
        public void apply(LinkedList<Token> tokens) {
            // do nothing
        }

        public abstract int eagerlyReadFromString(String string, int pos);

    }
}