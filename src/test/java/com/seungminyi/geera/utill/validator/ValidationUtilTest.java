package com.seungminyi.geera.utill.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

class ValidationUtilTest {

    @Test
    @DisplayName("바인딩 에러 핸들러")
    void testHandleBindingErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
            new FieldError("objectName", "fieldName1", "errorMessage1"),
            new FieldError("objectName", "fieldName2", "errorMessage2")
        ));

        ResponseEntity<?> responseEntity = ValidationUtil.handleBindingErrors(bindingResult);

        assertNotNull(responseEntity);
        assertEquals(400, responseEntity.getStatusCodeValue());

        List<String> errorMessages = (List<String>) responseEntity.getBody();
        assertNotNull(errorMessages);
        assertEquals(2, errorMessages.size());
        assertTrue(errorMessages.contains("errorMessage1"));
        assertTrue(errorMessages.contains("errorMessage2"));
    }
}