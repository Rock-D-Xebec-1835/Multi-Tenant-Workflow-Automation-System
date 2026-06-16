package com.mtwas.workflow_service.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtwas.workflow_service.entity.TaskInstance;
import com.mtwas.workflow_service.entity.WorkflowInstance;
import com.mtwas.workflow_service.entity.WorkflowVersion;
import com.mtwas.workflow_service.enums.NodeType;
import com.mtwas.workflow_service.enums.TaskStatus;
import com.mtwas.workflow_service.exception.TaskDispatchException;
import com.mtwas.workflow_service.repository.TaskInstanceRepository;
import com.mtwas.workflow_service.repository.WorkflowVersionRepository;
import com.mtwas.workflow_service.service.executor.HttpTaskExecutor;
import com.mtwas.workflow_service.validation.NodeDsl;
import com.mtwas.workflow_service.validation.WorkflowDsl;

@Service
public class TaskExecutionServiceImpl implements TaskExecutionService{
	
	private final TaskInstanceRepository taskInstanceRepository;
	private final WorkflowVersionRepository workflowVersionRepository;
	private final HttpTaskExecutor httpTaskExecutor;
	private final ObjectMapper objectMapper;
	@Lazy
	private final TaskCompletionService taskCompletionService;
	
	public TaskExecutionServiceImpl(TaskInstanceRepository taskInstanceRepository, WorkflowVersionRepository workflowVersionRepository, HttpTaskExecutor httpTaskExecutor, ObjectMapper objectMapper, @Lazy TaskCompletionService taskCompletionService) {
		this.taskInstanceRepository = taskInstanceRepository;
		this.workflowVersionRepository = workflowVersionRepository;
		this.httpTaskExecutor = httpTaskExecutor;
		this.objectMapper = objectMapper;
		this.taskCompletionService = taskCompletionService;
	}
	
	@Override
	public void executeTask(Long taskId) {
		TaskInstance task = taskInstanceRepository.findById(taskId).orElseThrow(() -> new TaskDispatchException("Task not found: " + taskId));
		
		if(task.getStatus() != TaskStatus.PENDING) throw new TaskDispatchException("Only PENDING tasks can be executed");
		
		WorkflowInstance workflowInstance = task.getWorkflowInstance();
		WorkflowVersion workflowVersion = workflowVersionRepository.findByWorkflowDefinitionIdAndVersion(workflowInstance.getWorkflowDefinition().getId(), workflowInstance.getWorkflowVersion()).orElseThrow(() -> new TaskDispatchException("Workflow version not found"));
		
		WorkflowDsl workflowDsl;
		
		try {
			workflowDsl = objectMapper.readValue(workflowVersion.getDefinitionJson(), WorkflowDsl.class);
		}
		catch (Exception e) {
			throw new TaskDispatchException("Unable to parse workflow definition");
		}
		
		NodeDsl currentNode = workflowDsl.nodes()
				.stream()
				.filter(node -> node.id().equals(task.getNodeId()))
				.findFirst()
				.orElseThrow(() -> new TaskDispatchException("Node not found"));
		
		if(NodeType.HTTP_TASK.name().equals(currentNode.type())) {
			httpTaskExecutor.execute(task, currentNode);
			taskCompletionService.completeTask(task.getId());
			return;
		}
		
		if(NodeType.MANUAL_TASK.name().equals(currentNode.type())) {
			return;
		}
		
		throw new TaskDispatchException("Unsupported node type: " + currentNode.type());
		
	}
}
