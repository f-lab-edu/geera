package com.seungminyi.geera.core.gql.generator;

import com.seungminyi.geera.core.gql.ast.GeeraCondition;
import com.seungminyi.geera.core.gql.ast.GeeraField;
import com.seungminyi.geera.core.gql.ast.GeeraKeyword;
import com.seungminyi.geera.core.gql.ast.GeeraOperation;
import com.seungminyi.geera.core.gql.ast.GeeraQuery;
import com.seungminyi.geera.core.gql.ast.GeeraValue;

public interface AstVisitor {
    String visit(GeeraQuery node);

    String visit(GeeraCondition node);

    String visit(GeeraKeyword node);

    String visit(GeeraField node);

    String visit(GeeraValue node);

    String visit(GeeraOperation node);
}
