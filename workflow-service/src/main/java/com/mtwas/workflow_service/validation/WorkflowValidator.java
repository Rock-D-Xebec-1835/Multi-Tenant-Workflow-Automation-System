package com.mtwas.workflow_service.validation;

public interface WorkflowValidator {
	ValidationResult validate(String workflowJson);
}
