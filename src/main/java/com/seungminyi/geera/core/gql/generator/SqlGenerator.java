package com.seungminyi.geera.core.gql.generator;

import java.util.stream.Collectors;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableMap;
import com.seungminyi.geera.core.gql.ast.GeeraCondition;
import com.seungminyi.geera.core.gql.ast.GeeraField;
import com.seungminyi.geera.core.gql.ast.GeeraKeyword;
import com.seungminyi.geera.core.gql.ast.GeeraOperation;
import com.seungminyi.geera.core.gql.ast.GeeraQuery;
import com.seungminyi.geera.core.gql.ast.GeeraValue;
import com.seungminyi.geera.core.gql.ast.QueryNode;
import com.seungminyi.geera.core.gql.ast.ValueType;
import com.seungminyi.geera.exception.GqlUnknownFieldException;
import com.seungminyi.geera.exception.GqlUnsupportedOperationException;

public class SqlGenerator implements AstVisitor {

    private Class<?> entityClass;

    private static final ImmutableMap<GeeraOperation, String> operationSqlMap = ImmutableMap.<GeeraOperation, String>builder()
        .put(GeeraOperation.LT, "<")
        .put(GeeraOperation.LE, "<=")
        .put(GeeraOperation.GT, ">")
        .put(GeeraOperation.GE, ">=")
        .put(GeeraOperation.ASSIGN, "=")
        .put(GeeraOperation.IS, "IS")
        .put(GeeraOperation.IN, "IN")
        .put(GeeraOperation.NOT, "NOT")
        .put(GeeraOperation.NOT_IN, "NOT IN")
        .build();

    public SqlGenerator(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public String visit(GeeraQuery node) {
        StringBuilder sb = new StringBuilder();

        if (node == null) {
            return null;
        }

        for (QueryNode qn : node.getChildren()) {
            sb.append(qn.accept(this));
        }
        return sb.toString();
    }

    @Override
    public String visit(GeeraCondition node) {
        return buildConditionString(node);
    }

    @Override
    public String visit(GeeraKeyword node) {
        switch (node) {
            case AND:
                return "AND ";
            case OR:
                return "OR ";
            default:
                throw new GqlUnsupportedOperationException("지원되지 않는 키워드 입니다 :" + node);
        }
    }

    @Override
    public String visit(GeeraField node) {
        String fieldName = node.getName();
        String entityColumnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName);
        try {
            entityClass.getDeclaredField(entityColumnName);
            return fieldName;
        } catch (NoSuchFieldException e) {
            throw new GqlUnknownFieldException("정의되지 않은 필드 입니다 : " + fieldName);
        }
    }

    @Override
    public String visit(GeeraOperation node) {
        String op = operationSqlMap.get(node);
        if (op == null) {
            throw new GqlUnknownFieldException("지원되지 않는 연산자 입니다 : " + node);
        }
        return op;
    }

    @Override
    public String visit(GeeraValue node) {
        if (node.getType() == ValueType.STRING_LITERAL) {
            return "'" + node.getValue() + "'";
        }
        return node.getValue();
    }

    private String buildConditionString(GeeraCondition node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.getField().accept(this))
            .append(" ")
            .append(node.getOp().accept(this))
            .append(" ");

        if (node.getValues().size() > 1) {
            sb.append("(")
                .append(node.getValues().stream()
                    .map(value -> value.accept(this))
                    .collect(Collectors.joining(", ")))
                .append(")");
        } else {
            sb.append(node.getValues().get(0).accept(this));
        }
        sb.append("\n");
        return sb.toString();
    }
}
