package com.mtwas.workflow_service.service.context;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtwas.workflow_service.entity.WorkflowInstance;
import com.mtwas.workflow_service.repository.WorkflowInstanceRepository;

@Service
public class ExecutionContextServiceImpl implements ExecutionContextService{
	
	private final ObjectMapper objectMapper;
	private final WorkflowInstanceRepository workflowInstanceRepository;
	
	public ExecutionContextServiceImpl(ObjectMapper objectMapper, WorkflowInstanceRepository workflowInstanceRepository) {
		this.objectMapper = objectMapper;
		this.workflowInstanceRepository = workflowInstanceRepository;
	}
	
	@Override
	public Map<String, Object> getContext(WorkflowInstance workflowInstance){
		try {
			return objectMapper.readValue(workflowInstance.getExecutionContext(),
					new TypeReference<Map<String, Object>>() {
					});
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to deserialize execution context", e);
		}
	}
	
	@Override
	public void updateContext(WorkflowInstance workflowInstance, Map<String, Object> context) {
		try {
			workflowInstance.setExecutionContext(objectMapper.writeValueAsString(context));
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to update cexecution context", e);
		}
	}
}
