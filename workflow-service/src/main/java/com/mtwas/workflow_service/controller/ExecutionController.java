package com.mtwas.workflow_service.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtwas.workflow_service.dto.ExecuteWorkflowRequest;
import com.mtwas.workflow_service.dto.ExecuteWorkflowResponse;
import com.mtwas.workflow_service.dto.ExecutionResponse;
import com.mtwas.workflow_service.dto.TaskInstanceResponse;
import com.mtwas.workflow_service.service.ExecutionService;


@RestController
@RequestMapping("/api/executions")
public class ExecutionController {
	
	private final ExecutionService executionService;
	public ExecutionController(ExecutionService executionService) {
		this.executionService = executionService;
	}
	
	@PostMapping("/workflows/{workflowId}")
	public ResponseEntity<ExecuteWorkflowResponse> executeWorkflow(@PathVariable Long workflowId, @RequestBody ExecuteWorkflowRequest request){
		return ResponseEntity.status(HttpStatus.CREATED).body(executionService.executeWorkflow(workflowId, request));
	}
	
	@GetMapping("/{executionId}")
	public ResponseEntity<ExecutionResponse> getExecution(@PathVariable Long executionId){
		return ResponseEntity.ok(executionService.getExecution(executionId));
	}
	
	@GetMapping("/{executionId}/tasks")
	public ResponseEntity<List<TaskInstanceResponse>> getExecutionTasks(@PathVariable Long executionId){
		return ResponseEntity.ok(executionService.getExecutionTasks(executionId));
	}
}
