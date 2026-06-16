package com.mtwas.workflow_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mtwas.workflow_service.entity.WorkflowInstance;

public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, Long>{

	Optional<WorkflowInstance> findById(Long id);
}
