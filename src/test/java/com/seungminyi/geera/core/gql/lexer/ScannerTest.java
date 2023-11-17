package com.seungminyi.geera.core.gql.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.EOFException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScannerTest {

	@Test
	void next() throws IOException {
		String query = """
			ISSUE_ID = 1
			AND PROJECT_ID = 1
			AND ISSUE_DESCRIPTION = "TEST ISSUE"
			""";
		Scanner scanner = new Scanner(query);

		for(char c : query.toCharArray()){
			char next = scanner.next();
			assertEquals(next, c);
		}

		assertThrows(EOFException.class, () -> scanner.next());
	}

	@Test
	void peek() throws IOException {
		String query = """
			ISSUE_ID = 1
			AND PROJECT_ID = 1
			AND ISSUE_DESCRIPTION = "TEST ISSUE"
			""";
		Scanner scanner = new Scanner(query);

		char beforePeek = scanner.peek();
		char afterPeek = scanner.peek();

		assertEquals(beforePeek, afterPeek);
	}
}