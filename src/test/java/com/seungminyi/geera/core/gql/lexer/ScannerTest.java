package com.seungminyi.geera.core.gql.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class ScannerTest {

    @Test
    void testNext() throws IOException {
        String query = "abc";
        Scanner scanner = new Scanner(query);

        assertEquals('a', scanner.next());
        assertEquals('b', scanner.next());
        assertEquals('c', scanner.next());
    }

    @Test
    void test() {
        char dd = (char) -1;
        if(dd == -1){
            System.out.println("dd");
        }
    }
}