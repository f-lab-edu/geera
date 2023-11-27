package com.seungminyi.geera.core.gql.ast;

import java.util.List;

import com.seungminyi.geera.core.gql.generator.ASTVisitor;

import lombok.Getter;

@Getter
public class GeeraQuery implements ASTNode {
    private final List<QueryNode> children;

    public GeeraQuery(List<QueryNode> children) {
        this.children = children;
    }

    @Override
    public String accept(ASTVisitor v) {
        return v.visit(this);
    }
}
