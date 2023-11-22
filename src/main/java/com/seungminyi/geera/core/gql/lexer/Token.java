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
            case FIELD, INT_LITERAL:
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


		FIELD,

        EOF
    }

}
