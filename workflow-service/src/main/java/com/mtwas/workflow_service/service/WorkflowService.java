package com.mtwas.workflow_service.service;

import java.util.List;

import com.mtwas.workflow_service.dto.CreateWorkflowRequest;
import com.mtwas.workflow_service.dto.CreateWorkflowVersionRequest;
import com.mtwas.workflow_service.dto.WorkflowResponse;
import com.mtwas.workflow_service.dto.WorkflowVersionResponse;
import com.mtwas.workflow_service.validation.ValidationResult;

public interface WorkflowService {
	WorkflowResponse createWorkflow(
			CreateWorkflowRequest request
	);
	
	WorkflowResponse getWorkflow(Long workflowId);
	
	List<WorkflowResponse> getAllWorkflows();
	
	WorkflowVersionResponse createWorkflowVersion(Long workflowId, CreateWorkflowVersionRequest request);
	
	WorkflowVersionResponse publishWorkflowVersion(Long workflowId, Integer version);
	
	List<WorkflowVersionResponse> getAllWorkflowVersions(Long workflowId);
	
	ValidationResult validateWorkflowVersion(Long workflowId, Integer vision);
	
}

