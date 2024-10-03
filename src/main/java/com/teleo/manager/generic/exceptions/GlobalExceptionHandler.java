package com.teleo.manager.generic.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RefreshScope
@ResponseBody
@ControllerAdvice
@CrossOrigin("*")
public class GlobalExceptionHandler {

    // handler specific exceptions
    @ExceptionHandler(RessourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RessourceResponse<ErrorDetails>> handleResourceNotFoundException(RessourceNotFoundException exception, WebRequest request, Locale locale) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(), request.getDescription(true));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RessourceResponse<>(false, errorDetails));
    }

    // handler BadRequest
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RessourceResponse<ErrorDetails>> handleBadAttribeExceptionException(HttpClientErrorException.BadRequest exception, WebRequest request, Locale locale) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RessourceResponse<>(false, errorDetails));
    }

    // handler global exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<RessourceResponse<ErrorDetails>> handleGlobalException(Exception exception, WebRequest request, Locale locale) {
        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RessourceResponse<>(false, errorDetails));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RessourceResponse<List<ErrorDetails>>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request, Locale locale) {
        List<ErrorDetails> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            ErrorDetails errorDetails = new ErrorDetails(errorMessage, request.getDescription(false));
            errors.add(errorDetails);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RessourceResponse<>(false, errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RessourceResponse<ErrorDetails>> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, WebRequest request, Locale locale) {
        String errorDetails = "JSON inacceptable " + exception.getMessage();
        if (exception.getCause() instanceof InvalidFormatException ifx) {
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                errorDetails = String.format("Valeur d'énumération invalide :'%s' pour '%s'. La valeur doit être l'une parmi les suivantes : %s.",
                        ifx.getValue(), ifx.getPath().get(ifx.getPath().size()-1).getFieldName(), Arrays.toString(ifx.getTargetType().getEnumConstants()));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RessourceResponse<>(false, errorDetails));
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<RessourceResponse<String>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RessourceResponse<>(false, "Erreur d'authentification. Cause : " + ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<RessourceResponse<String>> handleTokenAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RessourceResponse<>(false, "Erreur d'authentification. Cause : " + ex.getMessage()));
    }
}
