package com.seungminyi.geera.utill.text;

public class CaseConverter {
    public static String snakeToCamel(String snake) {
        StringBuilder camelCase = new StringBuilder();
        boolean capitalizeNext = false;

        snake = snake.toLowerCase();

        for (char c : snake.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                camelCase.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                camelCase.append(c);
            }
        }

        return camelCase.toString();
    }
}
