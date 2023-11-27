package com.seungminyi.geera.core.gql.ast;

import com.seungminyi.geera.core.gql.generator.ASTVisitor;

import lombok.Getter;

@Getter
public class GeeraValue implements ASTNode {
    private final String value;
    private final ValueType type;

    public GeeraValue(String value, ValueType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String accept(ASTVisitor v) {
        return v.visit(this);
    }
}
