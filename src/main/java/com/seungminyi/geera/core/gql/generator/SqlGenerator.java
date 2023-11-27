package com.seungminyi.geera.core.gql.generator;

import java.util.stream.Collectors;

import com.seungminyi.geera.core.gql.ast.Condition;
import com.seungminyi.geera.core.gql.ast.GeeraField;
import com.seungminyi.geera.core.gql.ast.GeeraKeyword;
import com.seungminyi.geera.core.gql.ast.GeeraOperation;
import com.seungminyi.geera.core.gql.ast.GeeraQuery;
import com.seungminyi.geera.core.gql.ast.GeeraValue;
import com.seungminyi.geera.core.gql.ast.QueryNode;
import com.seungminyi.geera.core.gql.ast.ValueType;
import com.seungminyi.geera.exception.GqlUnknownFieldException;
import com.seungminyi.geera.exception.GqlUnsupportedOperationException;
import com.seungminyi.geera.utill.text.CaseConverter;

public class SqlGenerator implements ASTVisitor {

    private Class<?> entityClass;

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
    public String visit(Condition node) {
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
        String EntityColumnName = CaseConverter.snakeToCamel(fieldName);
        try {
            entityClass.getDeclaredField(EntityColumnName);
            return fieldName;
        } catch (NoSuchFieldException e) {
            throw new GqlUnknownFieldException("정의되지 않은 필드 입니다 : " + fieldName);
        }
    }

    @Override
    public String visit(GeeraOperation node) {
        switch (node) {
            case LT:
                return "<";
            case GT:
                return ">";
            case ASSIGN:
                return "=";
            case LE:
                return "<=";
            case GE:
                return ">=";
            case IS:
                return "IS";
            case NOT:
                return "NOT";
            case IN:
                return "IN";
            case NOT_IN:
                return "NOT IN";
            default:
                return null;
        }
    }

    @Override
    public String visit(GeeraValue node) {
        if (node.getType() == ValueType.STRING_LITERAL) {
            return "'" + node.getValue() + "'";
        }
        return node.getValue();
    }

    private String buildConditionString(Condition node) {
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
