package com.seungminyi.geera.core.gql.ast;

import java.util.List;

import com.seungminyi.geera.core.gql.generator.AstVisitor;

import lombok.Getter;

@Getter
public class GeeraQuery implements AstNode {
    private final List<QueryNode> children;

    public GeeraQuery(List<QueryNode> children) {
        this.children = children;
    }

    @Override
    public String accept(AstVisitor visitor) {
        return visitor.visit(this);
    }
}
