package com.seungminyi.geera.core.gql.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.seungminyi.geera.core.gql.ast.GeeraCondition;
import com.seungminyi.geera.core.gql.ast.GeeraField;
import com.seungminyi.geera.core.gql.ast.GeeraKeyword;
import com.seungminyi.geera.core.gql.ast.GeeraOperation;
import com.seungminyi.geera.core.gql.ast.GeeraQuery;
import com.seungminyi.geera.core.gql.ast.GeeraValue;
import com.seungminyi.geera.core.gql.ast.ValueType;
import com.seungminyi.geera.exception.GqlParseException;
import com.seungminyi.geera.issue.dto.Issue;

class SqlGeneratorTest {
    SqlGenerator sqlGenerator;

    @BeforeEach
    void setupParser() throws IOException {
        sqlGenerator = new SqlGenerator(Issue.class);
    }

    @Test
    @DisplayName("SQLGenerator - value (String)")
    void testVisitValue_StringLiteral() {
        GeeraValue value = new GeeraValue("TEST", ValueType.STRING_LITERAL);

        String sqlValue = sqlGenerator.visit(value);

        assertEquals(sqlValue, "'TEST'");
    }

    @Test
    @DisplayName("SQLGenerator - value (Int)")
    void testVisitValue_IntLiteral() {
        GeeraValue value = new GeeraValue("123", ValueType.INT_LITERAL);

        String sqlValue = sqlGenerator.visit(value);

        assertEquals(sqlValue, "123");
    }

    @Test
    @DisplayName("SQLGenerator - Operation")
    void testVisitOp() {
        GeeraOperation op = GeeraOperation.ASSIGN;

        String sqlOp = sqlGenerator.visit(op);

        assertEquals(sqlOp, "=");
    }

    @Test
    @DisplayName("SQLGenerator - Field")
    void testVisitField() {
        GeeraField field = new GeeraField("PROJECT_ID");

        String sqlField = sqlGenerator.visit(field);

        assertEquals(sqlField, "PROJECT_ID");
    }

    @Test
    @DisplayName("SQLGenerator - 없는 Field")
    void testVisitField_Failure() {
        GeeraField field = new GeeraField("TEST");

        assertThrows(GqlParseException.class, () -> sqlGenerator.visit(field));
    }

    @Test
    @DisplayName("SQLGenerator - Keyword")
    void testVisitKeyword() {
        GeeraKeyword keyword = GeeraKeyword.AND;

        String visit = sqlGenerator.visit(keyword);

        assertEquals(visit, "AND ");
    }

    @Test
    @DisplayName("SQLGenerator - Condition")
    void testVisitCondition() {
        GeeraField field = new GeeraField("PROJECT_ID");
        GeeraOperation op = GeeraOperation.ASSIGN;
        GeeraValue value = new GeeraValue("123", ValueType.INT_LITERAL);
        GeeraCondition condition = new GeeraCondition(field, op, Arrays.asList(value));

        String sqlCondition = sqlGenerator.visit(condition);

        assertEquals(sqlCondition, "PROJECT_ID = 123\n");
    }

    @Test
    @DisplayName("SQLGenerator - GeeraQuery")
    void testVisitQuery() {
        GeeraField field1 = new GeeraField("PROJECT_ID");
        GeeraOperation op1 = GeeraOperation.ASSIGN;
        GeeraValue value1 = new GeeraValue("123", ValueType.INT_LITERAL);
        GeeraCondition condition1 = new GeeraCondition(field1, op1, Arrays.asList(value1));
        GeeraKeyword keyword = GeeraKeyword.OR;
        GeeraField field2 = new GeeraField("ISSUE_STATUS");
        GeeraOperation op2 = GeeraOperation.IN;
        GeeraValue value2 = new GeeraValue("TODO", ValueType.STRING_LITERAL);
        GeeraValue value3 = new GeeraValue("DONE", ValueType.STRING_LITERAL);
        GeeraCondition condition2 = new GeeraCondition(field2, op2, Arrays.asList(value2, value3));
        GeeraQuery query = new GeeraQuery(Arrays.asList(condition1, keyword, condition2));

        String sqlCondition = sqlGenerator.visit(query);

        assertEquals(sqlCondition, "PROJECT_ID = 123\nOR ISSUE_STATUS IN ('TODO', 'DONE')\n");
    }
}