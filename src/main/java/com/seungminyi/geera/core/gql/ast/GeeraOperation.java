package com.seungminyi.geera.core.gql.ast;

import com.seungminyi.geera.core.gql.generator.AstVisitor;

public enum GeeraOperation implements AstNode {
    LT,
    LE,
    GT,
    GE,
    ASSIGN,
    IS,
    IN,
    NOT,
    NOT_IN;

    @Override
    public String accept(AstVisitor visitor) {
        return visitor.visit(this);
    }
}
