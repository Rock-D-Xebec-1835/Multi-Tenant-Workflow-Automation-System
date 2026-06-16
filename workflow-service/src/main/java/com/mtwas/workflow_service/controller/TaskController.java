package com.mtwas.workflow_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtwas.workflow_service.service.TaskCompletionService;
import com.mtwas.workflow_service.service.TaskDispatcherService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
	private final TaskDispatcherService taskDispatcherService;
	private final TaskCompletionService taskCompletionService;
	public TaskController(TaskDispatcherService taskDispatcherService, TaskCompletionService taskCompletionService) {
		this.taskDispatcherService = taskDispatcherService;
		this.taskCompletionService = taskCompletionService;
	}
	
	@PostMapping("/{taskId}/dispatch")
	public ResponseEntity<Void> dispatchTask(@PathVariable Long taskId){
		taskDispatcherService.dispatchTask(taskId);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/{taskId}/complete")
	public ResponseEntity<Void> completeTask(@PathVariable Long taskId){
		taskCompletionService.completeTask(taskId);
		return ResponseEntity.ok().build();
	}
}
