package com.geektext.wishlist.controller;

import com.geektext.wishlist.service.WishListException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

/** Turns errors into clear JSON messages like {"error": "..."} */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(WishListException.class)
    public ResponseEntity<Map<String, String>> handleBusiness(WishListException ex) {
        return ResponseEntity.status(ex.getStatus()).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", msg));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
                       org.springframework.http.converter.HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> handleBadInput(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid request: check the ids and JSON body."));
    }
}
