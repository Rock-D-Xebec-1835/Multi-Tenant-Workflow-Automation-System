package com.mtwas.workflow_service.entity;

import java.time.LocalDateTime;

import com.mtwas.workflow_service.enums.WorkflowStatus;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
		name = "workflow_version",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {
						"workflow_definition_id",
						"version"
				})
		}
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkflowVersion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflow_definition_id")
	private WorkflowDefinition workflowDefinition;
	@Column(nullable = false)
	private Integer version;
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private WorkflowStatus status;
	@Column(columnDefinition = "json")
	private String definitionJson;
	private LocalDateTime createdAt;
	
	@PreUpdate
	public void onUpdate() {
		createdAt = LocalDateTime.now();
	}
}
