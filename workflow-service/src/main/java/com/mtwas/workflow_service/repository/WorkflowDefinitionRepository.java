package com.mtwas.workflow_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mtwas.workflow_service.entity.WorkflowDefinition;

@Repository
public interface WorkflowDefinitionRepository extends JpaRepository<WorkflowDefinition, Long>{

}
