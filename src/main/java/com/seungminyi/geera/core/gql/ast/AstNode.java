package com.seungminyi.geera.core.gql.ast;

import com.seungminyi.geera.core.gql.generator.AstVisitor;

public interface AstNode {
    String accept(AstVisitor visitor);
}
