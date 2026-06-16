package com.mtwas.workflow_service.controller;

import java.util.Map;

import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtwas.workflow_service.enums.NodeType;
import com.mtwas.workflow_service.service.executor.HttpTaskExecutor;
import com.mtwas.workflow_service.validation.NodeDsl;

@RestController
@RequestMapping("/api/test")
public class TestController {
	private final HttpTaskExecutor executor;
	
	public TestController(HttpTaskExecutor executor) {
		this.executor = executor;
	}
	
	@PostMapping("/http")
	public String test() {
		executor.execute(null, new NodeDsl("fetchUser", "Fetch User", NodeType.HTTP_TASK.name(), Map.of(
					"method", "GET",
					"url",
					"https://jsonplaceholder.typicode.com/users/1"
				))
			);
		return "ok";
	}
}
