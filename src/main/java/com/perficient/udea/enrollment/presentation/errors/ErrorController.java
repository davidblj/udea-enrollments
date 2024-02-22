package com.perficient.udea.enrollment.presentation.errors;

import com.perficient.udea.enrollment.presentation.errors.DTOs.ErrorResponse;
import com.perficient.udea.enrollment.presentation.errors.DTOs.SimpleErrorResponse;
import com.perficient.udea.enrollment.presentation.errors.exceptions.EnrollmentInvalidSessionException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.ActiveEnrollmentException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.InvalidCourseTrayException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.NoSpotsAvailableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorController {


    @ExceptionHandler
    ResponseEntity<ErrorResponse> handleNoSpotsAvailableException(NoSpotsAvailableException exception){
        List<String> unavailableSpots = exception.getClassRoomsOutOfSpots().stream().map(classRoom -> classRoom.getId().toString()).toList();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.toString(), exception.getMessage(), unavailableSpots);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> handleInvalidCourseTrayException(InvalidCourseTrayException exception){
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getMessage(), exception.getInvalidClassRoomsIds());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> handleInvalidCourseEnrollmentException(ActiveEnrollmentException exception){
        SimpleErrorResponse errorResponse = new SimpleErrorResponse(HttpStatus.BAD_REQUEST.toString(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> handleEnrollmentInvalidSessionException(EnrollmentInvalidSessionException exception){
        SimpleErrorResponse errorResponse = new SimpleErrorResponse(HttpStatus.FORBIDDEN.toString(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler
    ResponseEntity<SimpleErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception){

        SimpleErrorResponse errorResponse = Optional.ofNullable(exception.getCause())
                .flatMap(throwable -> Optional.ofNullable(throwable.getCause()))
                .map(throwable -> new SimpleErrorResponse(HttpStatus.CONFLICT.toString(), throwable.getMessage()))
                .orElse(new SimpleErrorResponse(HttpStatus.CONFLICT.toString(), exception.getMessage()));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<Map<String, String>>> handleBindErrors(MethodArgumentNotValidException exception){

        List<Map<String, String>> errorList = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String > errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errorList);
    }
}
