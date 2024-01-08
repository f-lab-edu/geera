package com.seungminyi.geera.utill.validator;

import jakarta.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.parameters.P;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PasswordValidatorTest {

	private PasswordValidator passwordValidator;
	private ConstraintValidatorContext context;
	private ConstraintValidatorContext.ConstraintViolationBuilder builder;

	@BeforeEach
	void setUp() {
		passwordValidator = new PasswordValidator();
		context = mock(ConstraintValidatorContext.class);
		builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
		lenient().when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
	}

	@Test
	@DisplayName("유효한 패스워드")
	void testValidPassword() {
		String validPassword = "password1!";

		boolean isValid = passwordValidator.isValid(validPassword, context);

		assertTrue(isValid);
	}

	@Test
	@DisplayName("취약한 패스워드")
	void testInvalidPassword() {
		String invalidPassword = "password";

		boolean isValid = passwordValidator.isValid(invalidPassword, context);

		assertFalse(isValid);
		verify(context).disableDefaultConstraintViolation();
		verify(builder).addConstraintViolation();
	}
}