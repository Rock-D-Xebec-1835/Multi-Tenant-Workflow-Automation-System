package com.mtwas.workflow_service.exception;


public class ExecutionNotFoundException extends RuntimeException{
	public ExecutionNotFoundException(Long executionId) {
		super("Execution not found with Id: " + executionId);
	}
}
