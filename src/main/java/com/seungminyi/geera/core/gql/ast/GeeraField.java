package com.seungminyi.geera.core.gql.ast;

import com.seungminyi.geera.core.gql.generator.AstVisitor;

import lombok.Getter;

@Getter
public class GeeraField implements AstNode {
    private final String name;

    public GeeraField(String name) {
        this.name = name;
    }

    @Override
    public String accept(AstVisitor visitor) {
        return visitor.visit(this);
    }
}
