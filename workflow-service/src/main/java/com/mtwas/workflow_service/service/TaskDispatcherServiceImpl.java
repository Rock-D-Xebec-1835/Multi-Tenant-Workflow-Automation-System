package com.mtwas.workflow_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtwas.workflow_service.entity.TaskInstance;
import com.mtwas.workflow_service.entity.WorkflowInstance;
import com.mtwas.workflow_service.entity.WorkflowVersion;
import com.mtwas.workflow_service.enums.NodeType;
import com.mtwas.workflow_service.enums.TaskStatus;
import com.mtwas.workflow_service.enums.WorkflowExecutionStatus;
import com.mtwas.workflow_service.exception.TaskDispatchException;
import com.mtwas.workflow_service.repository.TaskInstanceRepository;
import com.mtwas.workflow_service.repository.WorkflowInstanceRepository;
import com.mtwas.workflow_service.repository.WorkflowVersionRepository;
import com.mtwas.workflow_service.validation.EdgeDsl;
import com.mtwas.workflow_service.validation.NodeDsl;
import com.mtwas.workflow_service.validation.WorkflowDsl;

import jakarta.transaction.Transactional;

@Service
public class TaskDispatcherServiceImpl implements TaskDispatcherService{
	private final TaskInstanceRepository taskInstanceRepository;

	private final WorkflowVersionRepository workflowVersionRepository;
	
	private final WorkflowInstanceRepository workflowInstanceRepository;
	
	private final TaskExecutionService taskExecutionService;
	
	@Lazy
	private final TaskCompletionService taskCompletionService;

	private final ObjectMapper objectMapper;

	public TaskDispatcherServiceImpl(TaskInstanceRepository taskInstanceRepository,
			WorkflowVersionRepository workflowVersionRepository, WorkflowInstanceRepository workflowInstanceRepository, TaskExecutionService taskExecutionService, @Lazy TaskCompletionService taskCompletionService, ObjectMapper objectMapper) {
		this.taskInstanceRepository = taskInstanceRepository;
		this.workflowVersionRepository = workflowVersionRepository;
		this.workflowInstanceRepository = workflowInstanceRepository;
		this.taskExecutionService = taskExecutionService;
		this.taskCompletionService = taskCompletionService;
		this.objectMapper = objectMapper;
	}
	
	@Override
	@Transactional
	public void dispatchTask(Long taskId) {
		TaskInstance currentTask = taskInstanceRepository.findById(taskId).orElseThrow(() -> new TaskDispatchException("Task not found: " + taskId));
		
		if(currentTask.getStatus() != TaskStatus.COMPLETED) throw new TaskDispatchException("Task is not dispatchable");
		
		WorkflowInstance workflowInstance = currentTask.getWorkflowInstance();
		
		if(NodeType.END.name().equals(currentTask.getNodeType())) {
			completeWorkflow(workflowInstance);
			return;
		}
		
		WorkflowVersion workflowVersion = workflowVersionRepository.findByWorkflowDefinitionIdAndVersion(workflowInstance.getWorkflowDefinition().getId(), workflowInstance.getWorkflowVersion()).orElseThrow(() -> new TaskDispatchException("Workflow version not found"));
		
		WorkflowDsl workflowDsl;
		
		try {
			workflowDsl = objectMapper.readValue(workflowVersion.getDefinitionJson(), WorkflowDsl.class);
		}
		catch (Exception ex) {
			throw new TaskDispatchException("Failed to parse the workflow definition");
		}
				
		createNextTask(workflowDsl, workflowInstance, currentTask.getNodeId());
		
		
	}

	
	private void createNextTask(WorkflowDsl workflowDsl, WorkflowInstance workflowInstance, String currentNodeId) {
		List<EdgeDsl> outgoingEdges = workflowDsl.edges()
				.stream()
				.filter(edge -> edge.from().equals(currentNodeId))
				.toList();
		
		for(EdgeDsl edge : outgoingEdges) {
			createTaskForNode(workflowDsl, workflowInstance, edge.to());
		}
	}
	
	private void createTaskForNode(WorkflowDsl workflowDsl, WorkflowInstance workflowInstance, String nodeId) {
		boolean alreadyExists = taskInstanceRepository.existsByWorkflowInstanceIdAndNodeId(workflowInstance.getId(), nodeId);
		
		if(alreadyExists) return;
		
		NodeDsl destinationNode = workflowDsl.nodes()
				.stream()
				.filter(node -> node.id().equals(nodeId))
				.findFirst()
				.orElseThrow(() -> new TaskDispatchException("Node not found: " + nodeId));
		
		TaskInstance nextTask = new TaskInstance();
		nextTask.setWorkflowInstance(workflowInstance);
		nextTask.setNodeId(destinationNode.id());
		nextTask.setNodeType(destinationNode.type());
		nextTask.setStatus(TaskStatus.PENDING);
		TaskInstance savedTask = taskInstanceRepository.save(nextTask);
		if(NodeType.HTTP_TASK.name().equals(savedTask.getNodeType())) {
			taskExecutionService.executeTask(savedTask.getId());
		}
		
		if(NodeType.END.name().equals(savedTask.getNodeType())) {
			taskCompletionService.completeTask(savedTask.getId());
		}
		
	}
	
	private void completeWorkflow(WorkflowInstance workflowInstance) {
		workflowInstance.setStatus(WorkflowExecutionStatus.COMPLETED);
		workflowInstance.setCompletedAt(LocalDateTime.now());
		workflowInstanceRepository.save(workflowInstance);
	}
	
}
