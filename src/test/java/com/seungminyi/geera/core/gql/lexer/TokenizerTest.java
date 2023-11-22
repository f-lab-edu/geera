package com.seungminyi.geera.core.gql.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.seungminyi.geera.core.gql.lexer.Token.TokenClass;

public class TokenizerTest {

	Scanner testScanner;
	Tokenizer testTokenizer;

	protected void setupTokenizer(String queryString) throws IOException {
		testScanner = new Scanner(queryString);
		testTokenizer = new Tokenizer(testScanner);
	}

	protected void assertTokenizerOutput(Token[] expectedOutput, String query) throws IOException {
		setupTokenizer(query);
		ArrayList<Token> currOutput = new ArrayList();
		for (Token token : expectedOutput) {
			currOutput.add(token);
			assertTokenEquals("Token 불일치 : " + token.position.getLine() + ":" + token.position.getColumn(),
				token, testTokenizer.nextToken());
		}

		assertFalse(currOutput.size() < expectedOutput.length);
		assertFalse(currOutput.size() > expectedOutput.length);
	}

	protected void assertTokenEquals(String message, Token expected, Token result) {
		assertAll(message,
			() -> {
				assertEquals(expected.data, result.data, "Token data 불일치");
			},
			() -> {
				assertEquals(expected.position, result.position, "Token position 불일치");
			},
			() -> {
				assertEquals(expected.tokenClass, result.tokenClass, "Token class 불일치");
			}
		);
	}

	@Test
	@DisplayName("GQL 토크나이저 - LPAR")
	public void testLPAR() throws IOException {
		String query = "(";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.LPAR, 1, 0),
			new Token(TokenClass.EOF, "", 1, 1)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - RPAR")
	public void testRPAR() throws IOException {
		String query = ")";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.RPAR, 1, 0),
			new Token(TokenClass.EOF, "", 1, 1)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - COMMA")
	public void testCOMMA() throws IOException {
		String query = ",";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.COMMA, 1, 0),
			new Token(TokenClass.EOF, "", 1, 1)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - ASSIGN")
	public void testASSIGN() throws IOException {
		String query = "=";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.ASSIGN, 1, 0),
			new Token(TokenClass.EOF, "", 1, 1)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - LT")
	public void testLT() throws IOException {
		String query = "<";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.LT, 1, 0),
			new Token(TokenClass.EOF, "", 1, 1)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - GT")
	public void testGT() throws IOException {
		String query = ">";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.GT, 1, 0),
			new Token(TokenClass.EOF, "", 1, 1)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - LE")
	public void testLE() throws IOException {
		String query = "<=";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.LE, 1, 0),
			new Token(TokenClass.EOF, "", 1, 2)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - GE")
	public void testGE() throws IOException {
		String query = ">=";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.GE, 1, 0),
			new Token(TokenClass.EOF, "", 1, 2)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - IS")
	public void testIS() throws IOException {
		String query = "IS";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.IS, 1, 0),
			new Token(TokenClass.EOF, "", 1, 2)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - NOT")
	public void testNOT() throws IOException {
		String query = "NOT";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.NOT, 1, 0),
			new Token(TokenClass.EOF, "", 1, 3)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - IN")
	public void testIN() throws IOException {
		String query = "IN";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.IN, 1, 0),
			new Token(TokenClass.EOF, "", 1, 2)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - NOT_IN")
	public void testNOT_IN() throws IOException {
		String query = "NOT IN";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.NOT_IN, 1, 0),
			new Token(TokenClass.EOF, "", 1, 6)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - AND")
	public void testAND() throws IOException {
		String query = "AND";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.AND, 1, 0),
			new Token(TokenClass.EOF, "", 1, 3)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - OR")
	public void testOR() throws IOException {
		String query = "OR";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.OR, 1, 0),
			new Token(TokenClass.EOF, "", 1, 2)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - ISSUE_ID")
	public void testISSUE_ID() throws IOException {
		String query = "ISSUE_ID";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.ISSUE_ID, 1, 0),
			new Token(TokenClass.EOF, "", 1, 8)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - PROJECT_ID")
	public void testPROJECT_ID() throws IOException {
		String query = "PROJECT_ID";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.PROJECT_ID, 1, 0),
			new Token(TokenClass.EOF, "", 1, 10)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - ISSUE_TYPE")
	public void testISSUE_TYPE() throws IOException {
		String query = "ISSUE_TYPE";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.ISSUE_TYPE, 1, 0),
			new Token(TokenClass.EOF, "", 1, 10)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - ISSUE_STATUS")
	public void testISSUE_STATUS() throws IOException {
		String query = "ISSUE_STATUS";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.ISSUE_STATUS, 1, 0),
			new Token(TokenClass.EOF, "", 1, 12)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - ISSUE_DESCRIPTION")
	public void testISSUE_DESCRIPTION() throws IOException {
		String query = "ISSUE_DESCRIPTION";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.ISSUE_DESCRIPTION, 1, 0),
			new Token(TokenClass.EOF, "", 1, 17)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - ISSUE_DETAIL")
	public void testISSUE_DETAIL() throws IOException {
		String query = "ISSUE_DETAIL";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.ISSUE_DETAIL, 1, 0),
			new Token(TokenClass.EOF, "", 1, 12)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - ISSUE_CONTRACT_ID")
	public void testISSUE_CONTRACT_ID() throws IOException {
		String query = "ISSUE_CONTRACT_ID";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.ISSUE_CONTRACT_ID, 1, 0),
			new Token(TokenClass.EOF, "", 1, 17)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - ISSUE_REPORTER_ID")
	public void testISSUE_REPORTER_ID() throws IOException {
		String query = "ISSUE_REPORTER_ID";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.ISSUE_REPORTER_ID, 1, 0),
			new Token(TokenClass.EOF, "", 1, 17)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - ISSUE_PRIORITY")
	public void testISSUE_PRIORITY() throws IOException {
		String query = "ISSUE_PRIORITY";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.ISSUE_PRIORITY, 1, 0),
			new Token(TokenClass.EOF, "", 1, 14)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - SPRINT_ID")
	public void testSPRINT_ID() throws IOException {
		String query = "SPRINT_ID";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.SPRINT_ID, 1, 0),
			new Token(TokenClass.EOF, "", 1, 9)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - CREATE_AT")
	public void testCREATE_AT() throws IOException {
		String query = "CREATE_AT";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.CREATE_AT, 1, 0),
			new Token(TokenClass.EOF, "", 1, 9)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - TOP_ISSUE")
	public void testTOP_ISSUE() throws IOException {
		String query = "TOP_ISSUE";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.TOP_ISSUE, 1, 0),
			new Token(TokenClass.EOF, "", 1, 9)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - 문자열 리터럴") //
	public void testSTRING_LITERAL() throws IOException {
		String query = "\"Test, 123 \t,\b \n \r \"";
		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.STRING_LITERAL, query, 1, 0),
			new Token(TokenClass.EOF, "", 2, 4)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저 - 정수 리터럴")
	public void testINT_LITERAL() throws IOException {
		String query = "1234567890";
		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.INT_LITERAL, "1234567890", 1, 0),
			new Token(TokenClass.EOF, "", 1, 10)
		}, query);
	}

	@Test
	@DisplayName("GQL 토크나이저")
	void testTokenizer() throws IOException {
		String query = """
				PROJECT_ID IN (1,2,3,4)
				AND ISSUE_ID = 1
				AND ISSUE_TYPE = "EPIC"
				AND ISSUE_STATUS NOT IN "DONE"
				AND ISSUE_DESCRIPTION = "이슈 요약"
				AND ISSUE_PRIORITY > 3
				OR CREATE_AT <= "0000-00-00"
			""";

		assertTokenizerOutput(new Token[] {
			new Token(TokenClass.PROJECT_ID, 1, 1),
			new Token(TokenClass.IN, 1, 12),
			new Token(TokenClass.LPAR, 1, 15),
			new Token(TokenClass.INT_LITERAL, "1", 1, 16),
			new Token(TokenClass.COMMA, 1, 17),
			new Token(TokenClass.INT_LITERAL, "2", 1, 18),
			new Token(TokenClass.COMMA, 1, 19),
			new Token(TokenClass.INT_LITERAL, "3", 1, 20),
			new Token(TokenClass.COMMA, 1, 21),
			new Token(TokenClass.INT_LITERAL, "4", 1, 22),
			new Token(TokenClass.RPAR, 1, 23),
			new Token(TokenClass.AND, 2, 1),
			new Token(TokenClass.ISSUE_ID, 2, 5),
			new Token(TokenClass.ASSIGN, 2, 14),
			new Token(TokenClass.INT_LITERAL, "1", 2, 16),
			new Token(TokenClass.AND, 3, 1),
			new Token(TokenClass.ISSUE_TYPE, 3, 5),
			new Token(TokenClass.ASSIGN, 3, 16),
			new Token(TokenClass.STRING_LITERAL, "\"EPIC\"", 3, 18),
			new Token(TokenClass.AND, 4, 1),
			new Token(TokenClass.ISSUE_STATUS, 4, 5),
			new Token(TokenClass.NOT_IN, 4, 18),
			new Token(TokenClass.STRING_LITERAL, "\"DONE\"", 4, 25),
			new Token(TokenClass.AND, 5, 1),
			new Token(TokenClass.ISSUE_DESCRIPTION, 5, 5),
			new Token(TokenClass.ASSIGN, 5, 23),
			new Token(TokenClass.STRING_LITERAL, "\"이슈 요약\"", 5, 25),
			new Token(TokenClass.AND, 6, 1),
			new Token(TokenClass.ISSUE_PRIORITY, 6, 5),
			new Token(TokenClass.GT, 6, 20),
			new Token(TokenClass.INT_LITERAL, "3", 6, 22),
			new Token(TokenClass.OR, 7, 1),
			new Token(TokenClass.CREATE_AT, 7, 4),
			new Token(TokenClass.LE, 7, 14),
			new Token(TokenClass.STRING_LITERAL, "\"0000-00-00\"", 7, 17),
		}, query);

	}
}