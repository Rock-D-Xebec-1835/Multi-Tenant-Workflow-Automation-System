package com.mtwas.workflow_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mtwas.workflow_service.entity.WorkflowVersion;


@Repository
public interface WorkflowVersionRepository extends JpaRepository<WorkflowVersion, Long>{
	Optional<WorkflowVersion> findTopByWorkflowDefinitionIdOrderByVersionDesc(Long workflowDefinitionId);
	Optional<WorkflowVersion> findByWorkflowDefinitionIdAndVersion(Long workflowId, Integer version);
	List<WorkflowVersion> findByWorkflowDefinitionIdOrderByVersionDesc(Long workflowId);
}
