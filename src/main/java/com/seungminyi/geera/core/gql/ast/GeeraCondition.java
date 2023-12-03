package com.seungminyi.geera.core.gql.ast;

import java.util.List;

import com.seungminyi.geera.core.gql.generator.AstVisitor;

import lombok.Getter;

@Getter
public class GeeraCondition implements QueryNode {
    private final GeeraField field;
    private final GeeraOperation op;
    private final List<GeeraValue> values;

    public GeeraCondition(GeeraField field, GeeraOperation op, List<GeeraValue> values) {
        this.field = field;
        this.op = op;
        this.values = values;
    }

    @Override
    public String accept(AstVisitor visitor) {
        return visitor.visit(this);
    }
}
