package com.mtwas.workflow_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtwas.workflow_service.dto.CreateWorkflowRequest;
import com.mtwas.workflow_service.dto.CreateWorkflowVersionRequest;
import com.mtwas.workflow_service.dto.WorkflowResponse;
import com.mtwas.workflow_service.dto.WorkflowVersionResponse;
import com.mtwas.workflow_service.entity.WorkflowDefinition;
import com.mtwas.workflow_service.entity.WorkflowVersion;
import com.mtwas.workflow_service.enums.WorkflowStatus;
import com.mtwas.workflow_service.exception.WorkflowNotFoundException;
import com.mtwas.workflow_service.exception.WorkflowValidationException;
import com.mtwas.workflow_service.exception.WorkflowVersionNotFoundException;
import com.mtwas.workflow_service.repository.WorkflowDefinitionRepository;
import com.mtwas.workflow_service.repository.WorkflowVersionRepository;
import com.mtwas.workflow_service.validation.ValidationResult;
import com.mtwas.workflow_service.validation.WorkflowValidator;

@Service
public class WorkflowServiceImpl implements WorkflowService{
	
	private final WorkflowDefinitionRepository workflowDefinitionRepository;
	private final WorkflowVersionRepository workflowVersionRepository;
	private final ObjectMapper objectMapper;
	private final WorkflowValidator workflowValidator;
	
	public WorkflowServiceImpl(WorkflowDefinitionRepository workflowDefinitionRepository, WorkflowVersionRepository workflowVersionRepository, ObjectMapper objectMapper, WorkflowValidator workflowValidator) {
		this.workflowDefinitionRepository = workflowDefinitionRepository;
		this.workflowVersionRepository = workflowVersionRepository;
		this.objectMapper = objectMapper;
		this.workflowValidator = workflowValidator;
	}
	
	@Override
	public WorkflowResponse createWorkflow(CreateWorkflowRequest request) {
		WorkflowDefinition workflowDefinition = new WorkflowDefinition();
		
		workflowDefinition.setTenantId(1L);
		workflowDefinition.setName(request.name());
		workflowDefinition.setDescription(request.description());
		
		WorkflowDefinition savedWorkflow = workflowDefinitionRepository.save(workflowDefinition);
		
		return new WorkflowResponse(
				savedWorkflow.getId(),
				savedWorkflow.getName(),
				savedWorkflow.getDescription(),
				savedWorkflow.getActiveVersion()
		);
	}
	
	@Override
	public WorkflowResponse getWorkflow(Long workflowId) {
		WorkflowDefinition workflow = workflowDefinitionRepository.findById(workflowId).orElseThrow(() -> new WorkflowNotFoundException(workflowId));
		
		return new WorkflowResponse(
				workflow.getId(),
				workflow.getName(),
				workflow.getDescription(),
				workflow.getActiveVersion()
		);
	}
	
	@Override
	public List<WorkflowResponse> getAllWorkflows(){
		return workflowDefinitionRepository.findAll()
				.stream()
				.map(workflow -> new WorkflowResponse(
						workflow.getId(),
						workflow.getName(),
						workflow.getDescription(),
						workflow.getActiveVersion()
				))
				.toList();
	}
	
	@Override
	public WorkflowVersionResponse createWorkflowVersion(Long workflowId, CreateWorkflowVersionRequest request) {
		WorkflowDefinition workflow = workflowDefinitionRepository.findById(workflowId).orElseThrow(() -> new WorkflowNotFoundException(workflowId));
		
		int nextVersion = workflowVersionRepository.findTopByWorkflowDefinitionIdOrderByVersionDesc(workflowId)
				.map(v -> v.getVersion() + 1)
				.orElse(1);
		
		WorkflowVersion workflowVersion = new WorkflowVersion();
		
		workflowVersion.setWorkflowDefinition(workflow);
		workflowVersion.setVersion(nextVersion);
		workflowVersion.setStatus(WorkflowStatus.DRAFT);
		try {
			workflowVersion.setDefinitionJson(
				objectMapper.writeValueAsString(request.definitionJson())
			);
		}catch (Exception e) {
			throw new RuntimeException("Failed to serialize workflow definition", e);
		}
		
		WorkflowVersion saved = workflowVersionRepository.save(workflowVersion);
		return new WorkflowVersionResponse(
				saved.getId(),
				saved.getVersion(),
				saved.getStatus().name()
		);
	}
	
	@Override
	public WorkflowVersionResponse publishWorkflowVersion(Long workflowId, Integer version) {
		WorkflowDefinition workflow = workflowDefinitionRepository.findById(workflowId).orElseThrow(() -> new WorkflowNotFoundException(workflowId));
		
		WorkflowVersion targetVersion = workflowVersionRepository.findByWorkflowDefinitionIdAndVersion(workflowId, version).orElseThrow(() -> new WorkflowVersionNotFoundException(workflowId, version));
		
		if(targetVersion.getStatus() != WorkflowStatus.DRAFT) throw new RuntimeException("Only DRAFT versions can be published");
		List<WorkflowVersion> versions = workflowVersionRepository.findByWorkflowDefinitionIdOrderByVersionDesc(workflowId);
		
		ValidationResult validationResult = workflowValidator.validate(targetVersion.getDefinitionJson());
		if(!validationResult.valid()) throw new WorkflowValidationException(validationResult.errors());
		
		versions.stream()
			.filter(v -> v.getStatus() == WorkflowStatus.PUBLISHED)
			.forEach(v-> v.setStatus(WorkflowStatus.ARCHIVED));
		
		targetVersion.setStatus(WorkflowStatus.PUBLISHED);
		
		workflow.setActiveVersion(version);
		workflowDefinitionRepository.save(workflow);
		workflowVersionRepository.saveAll(versions);
		workflowVersionRepository.save(targetVersion);
		
		return new WorkflowVersionResponse(
				targetVersion.getId(),
				targetVersion.getVersion(),
				targetVersion.getStatus().name()
		);
	}
	
	@Override
	public List<WorkflowVersionResponse> getAllWorkflowVersions(Long workflowId){
		workflowDefinitionRepository.findById(workflowId).orElseThrow(() -> new WorkflowNotFoundException(workflowId));
		
		return workflowVersionRepository.findByWorkflowDefinitionIdOrderByVersionDesc(workflowId)
				.stream()
				.map(version ->
					new WorkflowVersionResponse(
							version.getId(),
							version.getVersion(),
							version.getStatus().name()
					)
				)
				.toList();
	}
	
	@Override
	public ValidationResult validateWorkflowVersion(Long workflowId, Integer version) {
		WorkflowVersion workflowVersion = workflowVersionRepository.findByWorkflowDefinitionIdAndVersion(workflowId, version).orElseThrow(() -> new WorkflowVersionNotFoundException(workflowId, version));
		return workflowValidator.validate(workflowVersion.getDefinitionJson());
	}
	
	
	
	
}
