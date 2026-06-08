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

import com.mtwas.workflow_service.dto.CreateWorkflowRequest;
import com.mtwas.workflow_service.dto.CreateWorkflowVersionRequest;
import com.mtwas.workflow_service.dto.WorkflowResponse;
import com.mtwas.workflow_service.dto.WorkflowVersionResponse;
import com.mtwas.workflow_service.service.WorkflowService;
import com.mtwas.workflow_service.validation.ValidationResult;


@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {
	private final WorkflowService workflowService;
	
	public WorkflowController(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	
	@PostMapping
	public ResponseEntity<WorkflowResponse> createWorkflow(@RequestBody CreateWorkflowRequest request){
		WorkflowResponse response = workflowService.createWorkflow(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/{workflowId}")
	public ResponseEntity<WorkflowResponse> getWorkflow(@PathVariable Long workflowId){
		return ResponseEntity.ok(workflowService.getWorkflow(workflowId));
	}
	
	@GetMapping
	public ResponseEntity<List<WorkflowResponse>> getAllWorkflows(){
		return ResponseEntity.ok(workflowService.getAllWorkflows());
	}
	
	@PostMapping("/{workflowId}/versions")
	public ResponseEntity<WorkflowVersionResponse> createWorkflowVersion(@PathVariable Long workflowId, @RequestBody CreateWorkflowVersionRequest request){
		return ResponseEntity.status(HttpStatus.CREATED).body(
			workflowService.createWorkflowVersion(workflowId, request)
		);	
	}
	
	@PostMapping("/test")
	public ResponseEntity<String> test(
	        @RequestBody CreateWorkflowVersionRequest request) {

	    return ResponseEntity.ok("SUCCESS");
	}
	
	@PostMapping("/{workflowId}/versions/{version}/publish")
	public ResponseEntity<WorkflowVersionResponse> publishWorkflowVersion(@PathVariable Long workflowId, @PathVariable Integer version){
		return ResponseEntity.ok(workflowService.publishWorkflowVersion(workflowId, version));
	}
	
	@GetMapping("/{workflowId}/versions")
	public ResponseEntity<List<WorkflowVersionResponse>> getWorkflowVersions(@PathVariable Long workflowId){
		return ResponseEntity.ok(workflowService.getAllWorkflowVersions(workflowId));
	}
	
	@PostMapping("/{workflowId}/versions/{version}/validate")
	public ResponseEntity<ValidationResult> validateWorkflowVersion(@PathVariable Long workflowId, @PathVariable Integer version){
		return ResponseEntity.ok(workflowService.validateWorkflowVersion(workflowId, version));
	}
}
