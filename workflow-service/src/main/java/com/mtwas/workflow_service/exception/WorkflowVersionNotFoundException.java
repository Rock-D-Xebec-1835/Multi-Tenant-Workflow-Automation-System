package com.mtwas.workflow_service.exception;

public class WorkflowVersionNotFoundException extends RuntimeException {
	public WorkflowVersionNotFoundException(Long workflowId, Integer version) {
		super("Version" + version + " not found for workflow " + workflowId);
	}
}
