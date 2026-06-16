package com.mtwas.workflow_service.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WorkflowNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWorkflowNotFound(
            WorkflowNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    
    @ExceptionHandler(WorkflowVersionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWorkflowVersionNotFound(
            WorkflowVersionNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex) {
    	ex.printStackTrace();

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
    
    @ExceptionHandler(WorkflowValidationException.class)
    public ResponseEntity<ErrorResponse> handleWorkflowValidation(Exception ex){
    	ex.printStackTrace();
    	ErrorResponse error = new ErrorResponse(
    			LocalDateTime.now(),
    			HttpStatus.BAD_REQUEST.value(),
    			ex.getMessage());
    	
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(WorkflowExecutionException.class)
    public ResponseEntity<ErrorResponse> handleWorkflowExecution(Exception ex){
    	ex.printStackTrace();
    	ErrorResponse error = new ErrorResponse(
    			LocalDateTime.now(),
    			HttpStatus.INTERNAL_SERVER_ERROR.value(),
    			ex.getMessage());
    	
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    @ExceptionHandler(ExecutionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExecutionNotFound(Exception ex){
    	ex.printStackTrace();
    	ErrorResponse error = new ErrorResponse(
    			LocalDateTime.now(),
    			HttpStatus.NOT_FOUND.value(),
    			ex.getMessage());
    	
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(TaskDispatchException.class)
    public ResponseEntity<ErrorResponse> handleTaskDispatchException(TaskDispatchException ex){
    	ex.printStackTrace();
    	ErrorResponse error = new ErrorResponse(
    			LocalDateTime.now(),
    			HttpStatus.INTERNAL_SERVER_ERROR.value(),
    			ex.getMessage()
    	);
    	
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
}