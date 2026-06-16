package com.mtwas.workflow_service.service;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mtwas.workflow_service.entity.TaskInstance;
import com.mtwas.workflow_service.enums.TaskStatus;
import com.mtwas.workflow_service.exception.TaskDispatchException;
import com.mtwas.workflow_service.repository.TaskInstanceRepository;

import jakarta.transaction.Transactional;

@Service
public class TaskCompletionServiceImpl implements TaskCompletionService{
	
	private final TaskInstanceRepository taskInstanceRepository;
	private final TaskDispatcherService taskDispatcherService;
	
	public TaskCompletionServiceImpl(TaskInstanceRepository taskInstanceRepository, TaskDispatcherService taskDispatcherService) {
		this.taskInstanceRepository = taskInstanceRepository;
		this.taskDispatcherService = taskDispatcherService;
	}
	
	@Override
	@Transactional
	public void completeTask(Long taskId) {
		TaskInstance task = taskInstanceRepository.findById(taskId).orElseThrow(() -> new TaskDispatchException("Task not found: " + taskId));
		
		if(task.getStatus() != TaskStatus.PENDING) throw new TaskDispatchException("Only PENDING tasks can be completed");
		
		task.setStatus(TaskStatus.COMPLETED);
		task.setCompletedAt(LocalDateTime.now());
		
		taskInstanceRepository.save(task);
		
		taskDispatcherService.dispatchTask(taskId);
		
	}
	
}
