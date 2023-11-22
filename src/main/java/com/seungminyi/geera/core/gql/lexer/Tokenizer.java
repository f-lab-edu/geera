package com.seungminyi.geera.core.gql.lexer;

import java.io.EOFException;
import java.io.IOException;

import com.seungminyi.geera.core.gql.lexer.Token.TokenClass;
import com.seungminyi.geera.exception.UnrecognizedCharacterException;

public class Tokenizer {
    private final Scanner scanner;
    private char currentChar;
    private final StringBuilder tokenStringBuilder = new StringBuilder();
    private boolean reachedEof;
    int error;

    public Tokenizer(Scanner scanner) throws IOException {
        this.scanner = scanner;
        currentChar = scanner.next();
    }

    public Token nextToken() {
        Token result;

        try {
            result = next();
        } catch (EOFException ex) {
            return new Token(TokenClass.EOF, scanner.getLine(), scanner.getColumn());
        } catch (IOException e) {
            return null;
        }

        return result;
    }

    private Token next() throws IOException {
        int line = scanner.getLine();
        int column = scanner.getColumn() - 1;

        if (Character.isWhitespace(currentChar)) {
            currentChar = scanner.next();
            return next();
        }

        switch (currentChar) {
            // delimiters
            case ',':
                expectSingleChar(',');
                return new Token(TokenClass.COMMA, line, column);
            case '(':
                expectSingleChar('(');
                return new Token(TokenClass.LPAR, line, column);
            case ')':
                expectSingleChar(')');
                return new Token(TokenClass.RPAR, line, column);
            case '=':
                expectSingleChar('=');
                return new Token(TokenClass.ASSIGN, line, column);
            case '<':
                if (isFirstTokenMatch('<', "<=")) {
                    return new Token(TokenClass.LT, line, column);
                } else {
                    return new Token(TokenClass.LE, line, column);
                }
            case '>':
                if (isFirstTokenMatch('>', ">=")) {
                    return new Token(TokenClass.GT, line, column);
                } else {
                    return new Token(TokenClass.GE, line, column);
                }
        }

        if (isIntLiteral(currentChar)) {
            passIntLiteral();
            return new Token(TokenClass.INT_LITERAL, getTokenString(), line, column);
        }

        if (isStringLiteral(currentChar)) {
            passStringLiteral();
            return new Token(TokenClass.STRING_LITERAL, getTokenString(), line, column);
        }

        if (isChar(currentChar)) {
            String token = getFieldOrKeyword();
            return handleIdentifierOrKeyword(token, line, column);
        }

        currentChar = scanner.next();
        return null;
    }

    private String getTokenString() {
        final String tokenString = tokenStringBuilder.toString();
        clearTokenStringBuilder();
        return tokenString;
    }

    private void clearTokenStringBuilder() {
        tokenStringBuilder.setLength(0);
    }

    private boolean isFirstTokenMatch(char first, String second) throws IOException {
        acceptChar(currentChar);

        if (reachedEof) {
            return true;
        }

        if (second.charAt(1) == currentChar) {
            expectFullString(second.substring(1, second.length()));
            return false;
        } else {
            clearTokenStringBuilder();
            return true;
        }
    }

    private boolean isChar(char symbol) {
        return Character.isLetter(symbol) || symbol == '_'
            || (tokenStringBuilder.toString().equals("NOT") && symbol == ' ');
    }

    private boolean isStringLiteral(char symbol) {
        return symbol == '"';
    }

    private boolean isIntLiteral(char symbol) {
        return Character.isDigit(symbol);
    }

    private String getFieldOrKeyword() throws IOException {
        acceptChar(currentChar);

        while (isChar(currentChar)) {
            acceptChar(currentChar);
        }
        return getTokenString();
    }

    private void passIntLiteral() throws IOException {
        acceptChar(currentChar);
        while (Character.isDigit(currentChar)) {
            acceptChar(currentChar);
        }
    }

    private void acceptChar(char symbol) throws IOException {
        queueChar(symbol);
        try {
            currentChar = scanner.next();
        } catch (final EOFException e) {
            if (reachedEof) {
                throw e;
            } else {
                currentChar = (char)0;
                reachedEof = true;
            }
        }
    }

    private void queueChar(char symbol) {
        tokenStringBuilder.append(symbol);
    }

    private void passStringLiteral() throws IOException {
        if (currentChar != '"') {
            throw new UnrecognizedCharacterException("\"", currentChar, scanner.getLine(), scanner.getColumn());
        }
        acceptChar(currentChar);

        while (currentChar != '"') {
            acceptChar(currentChar);
        }
        acceptChar(currentChar);
    }

    private Token handleIdentifierOrKeyword(String token, int line, int column) throws IOException {
        switch (token) {
            case "IS":
                return new Token(TokenClass.IS, line, column);
            case "NOT":
                return new Token(TokenClass.NOT, line, column);
            case "NOT IN":
                return new Token(TokenClass.NOT_IN, line, column);
            case "IN":
                return new Token(TokenClass.IN, line, column);
            case "AND":
                return new Token(TokenClass.AND, line, column);
            case "OR":
                return new Token(TokenClass.OR, line, column);
            case "ISSUE_ID":
                return new Token(TokenClass.ISSUE_ID, line, column);
            case "PROJECT_ID":
                return new Token(TokenClass.PROJECT_ID, line, column);
            case "ISSUE_TYPE":
                return new Token(TokenClass.ISSUE_TYPE, line, column);
            case "ISSUE_STATUS":
                return new Token(TokenClass.ISSUE_STATUS, line, column);
            case "ISSUE_DESCRIPTION":
                return new Token(TokenClass.ISSUE_DESCRIPTION, line, column);
            case "ISSUE_DETAIL":
                return new Token(TokenClass.ISSUE_DETAIL, line, column);
            case "ISSUE_CONTRACT_ID":
                return new Token(TokenClass.ISSUE_CONTRACT_ID, line, column);
            case "ISSUE_REPORTER_ID":
                return new Token(TokenClass.ISSUE_REPORTER_ID, line, column);
            case "ISSUE_PRIORITY":
                return new Token(TokenClass.ISSUE_PRIORITY, line, column);
            case "SPRINT_ID":
                return new Token(TokenClass.SPRINT_ID, line, column);
            case "CREATE_AT":
                return new Token(TokenClass.CREATE_AT, line, column);
            case "TOP_ISSUE":
                return new Token(TokenClass.TOP_ISSUE, line, column);
            default:
                return null;
        }
    }

    private void expectSingleChar(char symbol) throws IOException {
        if (symbol != currentChar) {
            throw new UnrecognizedCharacterException("" + symbol, currentChar, scanner.getLine(), scanner.getColumn());
        }
        acceptChar(symbol);
        clearTokenStringBuilder();
    }

    private void expectFullString(String str) throws IOException {
        acceptChar(currentChar);
        for (char c : str.substring(1).toCharArray()) {
            if (c != currentChar) {
                throw new UnrecognizedCharacterException("" + c, currentChar, scanner.getLine(), scanner.getColumn());
            }
            acceptChar(c);
        }
        clearTokenStringBuilder();
    }

}
