package com.example.product_sales.handler;


import com.example.product_sales.constants.AppConstants;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Log4j2
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
            String reason = AppConstants.GENERAL_CAUSE;
            String message = ex.getMessage();
            log.info(AppConstants.EXCEPTION_LOG, ex.getClass().getName());
            log.info(AppConstants.REASON_LOG, ex.getMessage());
            final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, reason, message, LocalDateTime.now());
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<Object> handleAccessDenied(final AccessDeniedException ex, final WebRequest request) {

            String reason = AppConstants.ACCESS_DENIED;
            String message = "Unauthorized to perform this action";
            log.info(AppConstants.EXCEPTION_LOG, ex.getClass().getName());
            log.info(AppConstants.REASON_LOG, ex.getMessage());
            final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, reason, message, LocalDateTime.now());

            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        @ExceptionHandler({ IllegalArgumentException.class })
        public ResponseEntity<Object> illegalArgumentException(final IllegalArgumentException ex,
                                                               final WebRequest request) {
            log.info(AppConstants.EXCEPTION_LOG, ex.getClass().getName());
            log.info(AppConstants.REASON_LOG, ex.getMessage());
            final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, AppConstants.INVALID_ARGUMENT,
                    ex.getCause().getLocalizedMessage(), LocalDateTime.now());
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        @ExceptionHandler(NoSuchElementException.class)
        public ResponseEntity<Object> noSuchElement(final Exception ex, final WebRequest request) {
            String reason = AppConstants.ERROR_RESPONSE_MESSAGE_CODE;
            String message = ex.getMessage();
            log.info(AppConstants.EXCEPTION_LOG, ex.getClass().getName());
            log.info(AppConstants.REASON_LOG, ex.getMessage());
            final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, reason, message, LocalDateTime.now());
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
            List<String> errors = new ArrayList<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                errors.add(error.getDefaultMessage());
            });
            String reason = AppConstants.ERROR_RESPONSE_MESSAGE_CODE;
            String message = String.join(", ", errors);
            log.info(AppConstants.EXCEPTION_LOG, ex.getClass().getName());
            log.info(AppConstants.REASON_LOG, ex.getMessage());
            final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, reason, message, LocalDateTime.now());
            return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
        }

        @NonNull
        public static Throwable getRootCause(@NonNull Throwable t) {
            Throwable rootCause = NestedExceptionUtils.getRootCause(t);
            return rootCause != null ? rootCause : t;
        }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        String reason = "Resource not found";
        String message = ex.getMessage();
        log.info(AppConstants.EXCEPTION_LOG, ex.getClass().getName());
        log.info(AppConstants.REASON_LOG, ex.getMessage());
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, reason, message, LocalDateTime.now());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    }

