package com.seungminyi.geera.core.gql.ast;

import com.seungminyi.geera.core.gql.generator.ASTVisitor;

public enum GeeraOperation implements ASTNode{
    LT, LE, GT, GE, ASSIGN, IS, IN, NOT, NOT_IN;
    @Override
    public String accept(ASTVisitor v) {
        return v.visit(this);
    }
}
