package com.seungminyi.geera.core.gql.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.seungminyi.geera.core.gql.ast.GeeraQuery;
import com.seungminyi.geera.core.gql.lexer.Scanner;
import com.seungminyi.geera.core.gql.lexer.Tokenizer;
import com.seungminyi.geera.exception.GqlParseException;

class ParserTest {
    GqlParser testParser;
    Tokenizer testTokenizer;
    Scanner testScanner;

    void setupParser(String queryString) throws IOException {
        testScanner = new Scanner(queryString);
        testTokenizer = new Tokenizer(testScanner);
        testParser = new GqlParser(testTokenizer);
    }

    @Test
    @DisplayName("Parser 테스트")
    void testParse() throws IOException {
        String query = """
                PROJECT_ID = 1
            """;
        setupParser(query);

        GeeraQuery parseTree = testParser.parse();
        assertNotNull(parseTree);
        assertNotNull(parseTree.getChildren());
    }

    @Test
    @DisplayName("parser 테스트 - 여러 Condition")
    void testParsesMultipleConditions() throws IOException {
        String query = """
                PROJECT_ID = 1 AND ISSUE_TYPE IN ("TODO", "DONE")
            """;
        setupParser(query);

        GeeraQuery parseTree = testParser.parse();

        assertNotNull(parseTree);
        assertNotNull(parseTree.getChildren());
    }

    @Test
    @DisplayName("parser 테스트 - 빈 쿼리")
    void testParseEmpty() throws IOException {
        String query = """
            """;

        setupParser(query);
        GeeraQuery parseTree = testParser.parse();
        assertEquals(parseTree.getChildren(), null);
    }

    @Test
    @DisplayName("parser 테스트 - 구문오류")
    void testParserWithSyntaxError() throws IOException {
        String query = """
            PROJECT_ID = AND
            """;
        setupParser(query);

        assertThrows(GqlParseException.class, () -> testParser.parse());
    }
}