package com.seungminyi.geera.utill.text;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CaseConverterTest {
    @Test
    public void testSnakeToCamel_NormalCase() {
        String snakeCase = "snake_case";
        String expectedCamelCase = "snakeCase";

        assertEquals(expectedCamelCase, CaseConverter.snakeToCamel(snakeCase));
    }

    @Test
    public void testSnakeToCamel_UpperCaseInput() {
        String snakeCase = "UPPER_CASE";
        String expectedCamelCase = "upperCase";

        assertEquals(expectedCamelCase, CaseConverter.snakeToCamel(snakeCase));
    }

    @Test
    public void testSnakeToCamel_EmptyString() {
        String snakeCase = "";
        String expectedCamelCase = "";

        assertEquals(expectedCamelCase, CaseConverter.snakeToCamel(snakeCase));
    }

    @Test
    public void testSnakeToCamel_SingleWord() {
        String snakeCase = "singleword";
        String expectedCamelCase = "singleword";

        assertEquals(expectedCamelCase, CaseConverter.snakeToCamel(snakeCase));
    }
}