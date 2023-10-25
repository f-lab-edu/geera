package com.seungminyi.geera.utill.validator;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public class ValidationUtil {

	public static ResponseEntity<?> handleBindingErrors(BindingResult bindingResult) {
		List<String> errorMessages = bindingResult.getFieldErrors()
			.stream()
			.map(error -> error.getDefaultMessage())
			.collect(Collectors.toList());
		return ResponseEntity.badRequest().body(errorMessages);
	}
}
