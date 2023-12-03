package com.seungminyi.geera.core.gql.ast;

import com.seungminyi.geera.core.gql.generator.AstVisitor;

import lombok.Getter;

@Getter
public class GeeraValue implements AstNode {
    private final String value;
    private final ValueType type;

    public GeeraValue(String value, ValueType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String accept(AstVisitor visitor) {
        return visitor.visit(this);
    }
}
