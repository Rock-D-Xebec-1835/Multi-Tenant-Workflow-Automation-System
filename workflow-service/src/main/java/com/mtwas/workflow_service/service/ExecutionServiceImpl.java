package com.mtwas.workflow_service.service;

import java.util.List;

import org.hibernate.sql.exec.spi.ExecutionContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtwas.workflow_service.dto.ExecuteWorkflowRequest;
import com.mtwas.workflow_service.dto.ExecuteWorkflowResponse;
import com.mtwas.workflow_service.dto.ExecuteWorkflowResponse;
import com.mtwas.workflow_service.dto.ExecutionResponse;
import com.mtwas.workflow_service.dto.TaskInstanceResponse;
import com.mtwas.workflow_service.entity.TaskInstance;
import com.mtwas.workflow_service.entity.WorkflowDefinition;
import com.mtwas.workflow_service.entity.WorkflowInstance;
import com.mtwas.workflow_service.entity.WorkflowVersion;
import com.mtwas.workflow_service.enums.NodeType;
import com.mtwas.workflow_service.enums.TaskStatus;
import com.mtwas.workflow_service.enums.WorkflowExecutionStatus;
import com.mtwas.workflow_service.exception.ExecutionNotFoundException;
import com.mtwas.workflow_service.exception.WorkflowExecutionException;
import com.mtwas.workflow_service.exception.WorkflowNotFoundException;
import com.mtwas.workflow_service.repository.TaskInstanceRepository;
import com.mtwas.workflow_service.repository.WorkflowDefinitionRepository;
import com.mtwas.workflow_service.repository.WorkflowInstanceRepository;
import com.mtwas.workflow_service.repository.WorkflowVersionRepository;
import com.mtwas.workflow_service.validation.NodeDsl;
import com.mtwas.workflow_service.validation.WorkflowDsl;


@Service
public class ExecutionServiceImpl implements ExecutionService {

	private final WorkflowDefinitionRepository workflowDefinitionRepository;
	private final WorkflowVersionRepository workflowVersionRepository;
	private final WorkflowInstanceRepository workflowInstanceRepository;
	private final TaskInstanceRepository taskInstanceRepository;
	private final TaskCompletionService taskCompletionService;
	private final ObjectMapper objectMapper;
	
	public ExecutionServiceImpl(WorkflowDefinitionRepository workflowDefinitionRepository, WorkflowVersionRepository workflowVersionRepository, WorkflowInstanceRepository workflowInstanceRepository, TaskInstanceRepository taskInstanceRepository, TaskCompletionService taskCompletionService, ObjectMapper objectMapper) {
		this.workflowDefinitionRepository = workflowDefinitionRepository;
		this.workflowInstanceRepository = workflowInstanceRepository;
		this.workflowVersionRepository = workflowVersionRepository;
		this.taskInstanceRepository = taskInstanceRepository;
		this.taskCompletionService = taskCompletionService;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public ExecuteWorkflowResponse executeWorkflow(Long workflowId, ExecuteWorkflowRequest request) {
		WorkflowDefinition workflow = workflowDefinitionRepository.findById(workflowId).orElseThrow(() -> new WorkflowNotFoundException(workflowId));
		
		if(workflow.getActiveVersion() == null) throw new WorkflowExecutionException("Workflow has no published version");
		WorkflowVersion workflowVersion = workflowVersionRepository.findByWorkflowDefinitionIdAndVersion(workflowId, workflow.getActiveVersion()).orElseThrow(() -> new WorkflowExecutionException("Published workflow version not found"));
		
		WorkflowDsl workflowDsl;
		try {
			workflowDsl = objectMapper.readValue(workflowVersion.getDefinitionJson(), WorkflowDsl.class);
		}catch (Exception ex) {
			throw new WorkflowExecutionException("Unable to parse workflow definition" + ex.getMessage());
		}
		
		NodeDsl startNode = workflowDsl.nodes()
				.stream()
				.filter(node -> NodeType.START.name().equals(node.type()))
				.findFirst()
				.orElseThrow(() -> new WorkflowExecutionException("No START node found"));
		
		WorkflowInstance workflowInstance = new WorkflowInstance();
		workflowInstance.setTenantId(workflow.getTenantId());
		workflowInstance.setWorkflowDefinition(workflow);
		workflowInstance.setWorkflowVersion(workflow.getActiveVersion());
		workflowInstance.setStatus(WorkflowExecutionStatus.RUNNING);
		String executionContext;
		try {
			executionContext = objectMapper.writeValueAsString(request.context());
		} catch (JsonProcessingException e) {
			throw new WorkflowExecutionException("Failed to serialize execution context");
		}
		
		workflowInstance.setExecutionContext(executionContext);
		workflowInstance = workflowInstanceRepository.save(workflowInstance);
		
		TaskInstance taskInstance = new TaskInstance();
		taskInstance.setWorkflowInstance(workflowInstance);
		taskInstance.setNodeId(startNode.id());
		taskInstance.setNodeType(startNode.type());
		taskInstance.setStatus(TaskStatus.PENDING);
		
		taskInstance = taskInstanceRepository.save(taskInstance);
		taskCompletionService.completeTask(taskInstance.getId());
		
		return new ExecuteWorkflowResponse(workflowInstance.getId(), workflowInstance.getStatus().name());
	}
	
	@Override
	public ExecutionResponse getExecution(Long executionId) {
		WorkflowInstance workflowInstance = workflowInstanceRepository.findById(executionId).orElseThrow(() -> new WorkflowExecutionException("Workflow execution not found"));
		
		return new ExecutionResponse(
				workflowInstance.getId(),
				workflowInstance.getWorkflowDefinition().getId(),
				workflowInstance.getWorkflowVersion(),
				workflowInstance.getStatus().name(),
				workflowInstance.getStartedAt(),
				workflowInstance.getCompletedAt(),
				workflowInstance.getExecutionContext()
		);
	}
	
	@Override
	public List<TaskInstanceResponse> getExecutionTasks(Long executionId){
		workflowInstanceRepository.findById(executionId).orElseThrow(() -> new ExecutionNotFoundException(executionId));
		
		return taskInstanceRepository.findByWorkflowInstanceId(executionId)
				.stream()
				.map(task -> new TaskInstanceResponse(
						task.getId(),
						task.getNodeId(),
						task.getNodeType(),
						task.getStatus().name()
				))
				.toList();
	}
}
