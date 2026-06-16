package com.mtwas.workflow_service.entity;

import java.time.LocalDateTime;

import com.mtwas.workflow_service.enums.TaskStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_instance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskInstance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflow_instance_id")
	private WorkflowInstance workflowInstance;
	
	private String nodeId;
	private String nodeType;
	
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	
	private LocalDateTime startedAt;
	private LocalDateTime completedAt;
	
	@PrePersist
	public void onCreate() {
		if(status == null) status = TaskStatus.PENDING;
	}
}
