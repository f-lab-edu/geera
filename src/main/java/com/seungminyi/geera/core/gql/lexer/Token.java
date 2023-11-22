package com.seungminyi.geera.core.gql.lexer;

import com.seungminyi.geera.core.gql.lexer.dto.Position;

import lombok.ToString;

@ToString
public class Token {

    public final TokenClass tokenClass;
    public final String data;
    public final Position position;

    public Token(TokenClass tokenClass, String data, int lineNum, int colNum) {
        this.tokenClass = tokenClass;
        switch (tokenClass) {
            case STRING_LITERAL:
                this.data = data.substring(1, data.length() - 1);
                break;
            case INT_LITERAL:
                this.data = data;
                break;
            default:
                this.data = "";
        }
        this.position = new Position(lineNum, colNum);
    }

    public Token(TokenClass tokenClass, int lineNum, int colNum) {
        this(tokenClass, "", lineNum, colNum);
    }

    public enum TokenClass {

        // delimiters
        LPAR,  // '('
        RPAR,  // ')'
        COMMA, // ','

        // op
        LT, // '<'
        GT, // '>'
        ASSIGN, // '='
        LE, // "<="
        GE, // ">="
        IS, // "IS"
        NOT, // "NOT"
        IN, // "IN"
        NOT_IN, // "NOT IN"

        // value
        STRING_LITERAL, // \".*\"
        INT_LITERAL,    // ('0'|...|'9')+

        // keyword
        AND, // "AND"
        OR, // "OR"

        // field
        ISSUE_ID, // "ISSUE_ID"
        PROJECT_ID, // "PROJECT_ID"
        ISSUE_TYPE, // "ISSUE_TYPE"
        ISSUE_STATUS, // "ISSUE_STATUS"
        ISSUE_DESCRIPTION, // "ISSUE_DESCRIPTION"
        ISSUE_DETAIL, // "ISSUE_DETAIL"
        ISSUE_CONTRACT_ID, // "ISSUE_CONTRACT_ID"
        ISSUE_REPORTER_ID, // "ISSUE_REPORTER_ID"
        ISSUE_PRIORITY, // "ISSUE_PRIORITY"
        SPRINT_ID, // "SPRINT_ID"
        CREATE_AT, // "CREATE_AT"
        TOP_ISSUE, // "TOP_ISSUE"

        EOF
    }

}