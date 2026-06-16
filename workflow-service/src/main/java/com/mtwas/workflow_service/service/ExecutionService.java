package com.mtwas.workflow_service.service;


import java.util.List;

import com.mtwas.workflow_service.dto.ExecuteWorkflowRequest;
import com.mtwas.workflow_service.dto.ExecuteWorkflowResponse;
import com.mtwas.workflow_service.dto.ExecutionResponse;
import com.mtwas.workflow_service.dto.TaskInstanceResponse;

public interface ExecutionService {
	ExecuteWorkflowResponse executeWorkflow(Long workflowId, ExecuteWorkflowRequest request);
	
	ExecutionResponse getExecution(Long executionId);
	
	List<TaskInstanceResponse> getExecutionTasks(Long executionId);
}
