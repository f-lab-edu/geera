package com.seungminyi.geera.core.gql.ast;

import com.seungminyi.geera.core.gql.generator.ASTVisitor;

public enum GeeraKeyword implements QueryNode {

    AND, OR;

    @Override
    public String accept(ASTVisitor v) {
        return v.visit(this);
    }
}
