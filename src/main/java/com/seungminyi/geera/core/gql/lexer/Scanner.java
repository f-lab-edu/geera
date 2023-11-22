package com.seungminyi.geera.core.gql.lexer;

import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;

public class Scanner {
	private final StringReader gqlQueryStringReader;
	private int peekedVal = -1;
	private int line = 1;
	private int column = 0;

	public Scanner(String gqlQueryString) {
		this.gqlQueryStringReader = new StringReader(gqlQueryString);
	}

	public char next() throws IOException {

		Boolean havePeeked = peekedVal != -1;

		char nextChar;
		if (havePeeked) {
			nextChar = (char)peekedVal;
			peekedVal = -1;
		} else {
			int nextVal = gqlQueryStringReader.read();
			if (nextVal == -1) {
				throw new EOFException();
			}
			nextChar = (char)nextVal;
		}

		if (nextChar == '\n') {
			line++;
			column = 0;
		} else {
			column++;
		}

		return nextChar;
	}

	public char peek() throws IOException {

		Boolean havePeeked = peekedVal != -1;

		char peekedChar;
		if (havePeeked) {
			peekedChar = (char)peekedVal;
		} else {
			int readVal = gqlQueryStringReader.read();
			if (readVal == -1) {
				throw new EOFException();
			}
			peekedChar = (char)readVal;
			peekedVal = readVal;
		}

		return peekedChar;
	}

	public int getLine() {
		return this.line;
	}

	public int getColumn() {
		return this.column;
	}
}
