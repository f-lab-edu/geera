package com.seungminyi.geera.core.gql.lexer;

import com.seungminyi.geera.core.gql.lexer.Token.TokenClass;
import com.seungminyi.geera.exception.GqlSyntaxException;

import java.io.EOFException;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tokenizer {
	private final Scanner scanner;
	private char currentChar;
	private final StringBuilder tokenStringBuilder = new StringBuilder();
	int error;

	public Tokenizer(Scanner scanner) throws IOException {
		this.scanner = scanner;
		currentChar = scanner.next();
	}

	private Token nextToken() {
		Token result;

		try {
			result = next();
		}catch (EOFException ex) {
			return new Token(Token.TokenClass.EOF, scanner.getLine(), scanner.getColumn());
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}

		return result;
	}

	private Token next() throws IOException {
		int line = scanner.getLine();
		int column = scanner.getColumn() - 1;

		if(Character.isWhitespace(currentChar)) {
			currentChar = scanner.next();
			return next();
		}

		switch (currentChar) {
			// delimiters
			case ',':
				return new Token(TokenClass.COMMA, line, column);
			case '(':
				return new Token(TokenClass.LPAR, line, column);
			case ')':
				return new Token(TokenClass.RPAR, line, column);

			// operators
			case '>':

				return null;
			case '<':
				return null;

			case 'I':
		}

		return null;
	}

	private void expectFullString(String str, char currentChar) throws IOException {
		tokenStringBuilder.append(currentChar);
		for (char c: str.substring(1).toCharArray()) {
			if (c != scanner.next()) {
				throw new GqlSyntaxException(c, scanner.getLine(), scanner.getColumn());
			}
			tokenStringBuilder.append(c);
		}
	}




}
