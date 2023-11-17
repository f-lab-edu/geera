package com.seungminyi.geera.exception;

public class GqlSyntaxException extends RuntimeException{
	public GqlSyntaxException(char c, int line, int col){
		super("GQL Syntax Error: unrecognised character ("+c+") at "+line+":"+col);
	}
}
