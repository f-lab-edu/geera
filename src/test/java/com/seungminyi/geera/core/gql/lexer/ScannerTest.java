package com.seungminyi.geera.core.gql.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class ScannerTest {

	@Test
	void testPeek() throws IOException {
		String query = "abc";
		Scanner scanner = new Scanner(query);

		assertEquals('a', scanner.peek());
		assertEquals('a', scanner.next());
		assertEquals('b', scanner.peek());
		assertEquals('b', scanner.peek());
	}
}