package io.github.kevinlaig.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<String> errors = new ArrayList<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String errorMessage = error.getDefaultMessage();
      errors.add(errorMessage);
    });
    return ResponseEntity.badRequest().body(errors);
  }
}
