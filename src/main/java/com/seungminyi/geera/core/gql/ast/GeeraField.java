package com.seungminyi.geera.core.gql.ast;

import com.seungminyi.geera.core.gql.generator.ASTVisitor;

import lombok.Getter;

@Getter
public class GeeraField implements ASTNode{
    private final String name;

    public GeeraField(String name) {
        this.name = name;
    }

    @Override
    public String accept(ASTVisitor v) {
        return v.visit(this);
    }
}
