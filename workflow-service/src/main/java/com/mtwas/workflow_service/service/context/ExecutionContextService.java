package com.mtwas.workflow_service.service.context;

import java.util.Map;

import com.mtwas.workflow_service.entity.WorkflowInstance;

public interface ExecutionContextService {
	Map<String, Object> getContext(WorkflowInstance workflowInstance);
	
	void updateContext(WorkflowInstance workflowInstance, Map<String, Object> context);
}
