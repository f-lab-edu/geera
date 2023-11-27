package com.seungminyi.geera.core.gql.ast;

import com.seungminyi.geera.core.gql.generator.ASTVisitor;

public interface ASTNode {
    String accept(ASTVisitor v);
}