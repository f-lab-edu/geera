package com.seungminyi.geera.core.gql.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.seungminyi.geera.core.gql.ast.GeeraCondition;
import com.seungminyi.geera.core.gql.ast.GeeraField;
import com.seungminyi.geera.core.gql.ast.GeeraQuery;
import com.seungminyi.geera.core.gql.ast.GeeraKeyword;
import com.seungminyi.geera.core.gql.ast.GeeraOperation;
import com.seungminyi.geera.core.gql.ast.QueryNode;
import com.seungminyi.geera.core.gql.ast.GeeraValue;
import com.seungminyi.geera.core.gql.ast.ValueType;
import com.seungminyi.geera.core.gql.lexer.Token;
import com.seungminyi.geera.core.gql.lexer.Token.TokenClass;
import com.seungminyi.geera.core.gql.lexer.Tokenizer;
import com.seungminyi.geera.exception.GqlParseException;

public class GqlParser {
    private final Tokenizer tokenizer;
    private Token currToken;
    private static final Map<TokenClass, GeeraOperation> operationMap = new EnumMap<>(TokenClass.class);

    static {
        operationMap.put(TokenClass.LT, GeeraOperation.LT);
        operationMap.put(TokenClass.LE, GeeraOperation.LE);
        operationMap.put(TokenClass.GT, GeeraOperation.GT);
        operationMap.put(TokenClass.GE, GeeraOperation.GE);
        operationMap.put(TokenClass.ASSIGN, GeeraOperation.ASSIGN);
        operationMap.put(TokenClass.IS, GeeraOperation.IS);
        operationMap.put(TokenClass.IN, GeeraOperation.IN);
        operationMap.put(TokenClass.NOT, GeeraOperation.NOT);
        operationMap.put(TokenClass.NOT_IN, GeeraOperation.NOT_IN);
    }

    public GqlParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        nextToken();
    }

    private void nextToken() {
        currToken = tokenizer.nextToken();
    }

    public GeeraQuery parse() {
        if (currToken.tokenClass == TokenClass.EOF) {
            return new GeeraQuery(null);
        }
        return parseGeeraQuery();
    }

    private GeeraQuery parseGeeraQuery() {
        List<QueryNode> queryNodes = new ArrayList<>();
        do {
            queryNodes.add(parseQueryNode());
        } while (currToken.tokenClass != TokenClass.EOF);
        return new GeeraQuery(queryNodes);
    }

    private QueryNode parseQueryNode() {
        if (isKeyword(currToken.tokenClass)) {
            return parseKeyword();
        } else {
            return parseCondition();
        }
    }

    private GeeraKeyword parseKeyword() {
        GeeraKeyword keyword = GeeraKeyword.valueOf(currToken.tokenClass.name());
        nextToken();
        return keyword;
    }

    private GeeraCondition parseCondition() {
        GeeraField field = parseField();
        GeeraOperation operation = parseOp();
        List<GeeraValue> values = parseValues();
        return new GeeraCondition(field, operation, values);
    }

    private List<GeeraValue> parseValues() {
        List<GeeraValue> values = new ArrayList<>();

        if (accept(TokenClass.LPAR)) {
            nextToken();
        }

        values.add(parseValue());

        while (accept(TokenClass.COMMA)) {
            expect(TokenClass.COMMA);
            values.add(parseValue());
        }

        if (accept(TokenClass.RPAR)) {
            expect(TokenClass.RPAR);
        }
        return values;
    }

    private GeeraValue parseValue() {
        ValueType valueType = null;

        switch (currToken.tokenClass) {
            case STRING_LITERAL:
                valueType = ValueType.STRING_LITERAL;
                break;
            case INT_LITERAL:
                valueType = ValueType.INT_LITERAL;
                break;
            default:
                throw new GqlParseException("예상되지 않은 값입니다 : " + currToken.tokenClass);
        }

        GeeraValue value = new GeeraValue(currToken.data, valueType);
        nextToken();
        return value;
    }

    private GeeraOperation parseOp() {
        GeeraOperation operation = operationMap.get(currToken.tokenClass);
        if (operation == null) {
            throw new GqlParseException("지원되지 않는 연산자입니다 : " + currToken.tokenClass);
        }
        expect(currToken.tokenClass);
        return operation;
    }

    private GeeraField parseField() {
        if (currToken.tokenClass != TokenClass.FIELD) {
            throw new GqlParseException("FIELD가 위치해야 합니다 : " + currToken.tokenClass);
        }
        GeeraField field = new GeeraField(currToken.data);
        nextToken();
        return field;
    }

    private boolean accept(TokenClass... expected) {
        return Arrays.asList(expected).contains(currToken.tokenClass);
    }

    private void expect(TokenClass... expected) {
        if (!Arrays.asList(expected).contains(currToken.tokenClass)) {
            throw new GqlParseException("잘못된 구문입니다 : " + currToken.tokenClass);
        }
        nextToken();
    }

    private boolean isKeyword(TokenClass tokenClass) {
        return tokenClass == TokenClass.AND || tokenClass == TokenClass.OR;
    }
}
