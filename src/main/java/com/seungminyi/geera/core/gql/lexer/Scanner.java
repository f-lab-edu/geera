package com.seungminyi.geera.core.gql.lexer;

import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;

public class Scanner {
    private final StringReader gqlQueryStringReader;
    private int lineNo = 1;
    private int columnNo = 0;

    private boolean reachedEof = false;

    public Scanner(String gqlQueryString) {
        this.gqlQueryStringReader = new StringReader(gqlQueryString);
    }

    public char next() throws IOException {
        int nextVal = gqlQueryStringReader.read();
        char nextChar = (char)nextVal;

        if (nextVal == -1 ) {
            reachedEof = true;
        } else {
            if (nextChar == '\n') {
                lineNo++;
                columnNo = 0;
            } else {
                columnNo++;
            }
        }

        return nextChar;
    }

    public boolean isEof() {
        return reachedEof;
    }

    public int getLineNo() {
        return this.lineNo;
    }

    public int getColumnNo() {
        return this.columnNo;
    }
}
