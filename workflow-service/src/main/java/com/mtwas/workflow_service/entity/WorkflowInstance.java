package com.mtwas.workflow_service.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mtwas.workflow_service.enums.WorkflowExecutionStatus;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "workflow_instance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkflowInstance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long tenantId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflow_definition_id")
	private WorkflowDefinition workflowDefinition;
	private Integer workflowVersion;

	@Enumerated(EnumType.STRING)
	private WorkflowExecutionStatus status;
	
	private LocalDateTime startedAt;
	private LocalDateTime completedAt;
	
	@OneToMany(mappedBy = "workflowInstance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TaskInstance> taskInstances = new ArrayList<>();
	
	@Column(columnDefinition = "json")
	private String executionContext;
	
	@PrePersist
	public void onCreate() {
		startedAt = LocalDateTime.now();
		if(status == null) status = WorkflowExecutionStatus.CREATED;
	}
	
}
