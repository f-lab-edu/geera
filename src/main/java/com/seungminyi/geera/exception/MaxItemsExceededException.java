package com.seungminyi.geera.exception;

public class MaxItemsExceededException extends RuntimeException {
	public MaxItemsExceededException() {
		super("요청한 항목의 수가 최대 허용 범위를 초과했습니다. 최대 50개의 항목을 요청할 수 있습니다.");
	}
}
