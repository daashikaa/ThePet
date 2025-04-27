package com.thepet.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock private WebRequest webRequest;
    @InjectMocks private GlobalExceptionHandler exceptionHandler;

    @Test
    void handleResourceNotFoundException_ReturnsNotFoundResponse() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<Object> response = exceptionHandler.handleResourceNotFoundException(ex, webRequest);

        assertEquals(404, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Not Found"));
    }
}