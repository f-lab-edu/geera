package com.seungminyi.geera.core.gql.ast;

import com.seungminyi.geera.core.gql.generator.AstVisitor;

public enum GeeraKeyword implements QueryNode {

    AND, OR;

    @Override
    public String accept(AstVisitor visitor) {
        return visitor.visit(this);
    }
}
