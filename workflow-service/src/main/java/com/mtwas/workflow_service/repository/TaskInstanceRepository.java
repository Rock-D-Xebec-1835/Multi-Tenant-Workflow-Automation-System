package com.mtwas.workflow_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mtwas.workflow_service.entity.TaskInstance;

public interface TaskInstanceRepository extends JpaRepository<TaskInstance, Long> {
	List<TaskInstance> findByWorkflowInstanceId(Long workflowInstance);
	
	Optional<TaskInstance> findById(Long id);
	
	boolean existsByWorkflowInstanceIdAndNodeId(Long workflowInstanceId, String nodeId);
	
	
}

