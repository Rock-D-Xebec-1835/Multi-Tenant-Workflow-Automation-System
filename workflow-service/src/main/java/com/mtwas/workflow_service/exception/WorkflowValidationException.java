package com.mtwas.workflow_service.exception;

import java.util.List;

public class WorkflowValidationException extends RuntimeException{
	public WorkflowValidationException(List<String> errors) {
		super(String.join(", ", errors));
	}
}
