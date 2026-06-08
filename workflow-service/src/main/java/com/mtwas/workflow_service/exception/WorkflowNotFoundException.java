package com.mtwas.workflow_service.exception;

public class WorkflowNotFoundException extends RuntimeException{
	public WorkflowNotFoundException(Long workflowId) {
		super("Workflow not found with id: " + workflowId);
	}
}
