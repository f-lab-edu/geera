package com.seungminyi.geera.utill.validator;

import jakarta.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.BeforeEach;
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
	public void setUp() {
		passwordValidator = new PasswordValidator();
		context = mock(ConstraintValidatorContext.class);
		builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
		when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(builder);
	}

	@Test
	public void testValidPassword() {
		String validPassword = "password1!";

		boolean isValid = passwordValidator.isValid(validPassword, context);

		assertTrue(isValid);
	}

	@Test
	public void testInvalidPassword() {
		String invalidPassword = "password";

		boolean isValid = passwordValidator.isValid(invalidPassword, context);

		assertFalse(isValid);
		verify(context).disableDefaultConstraintViolation();
		verify(builder).addConstraintViolation();
	}
}