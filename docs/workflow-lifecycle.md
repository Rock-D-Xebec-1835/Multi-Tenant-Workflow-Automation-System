# Workflow Lifecycle

## Workflow Definition Lifecycle

A workflow definition represents a reusable template.

### States

```text
DRAFT
  |
  v
PUBLISHED
  |
  v
ARCHIVED
```

### DRAFT

* Editable
* Cannot be executed
* Can create multiple revisions

### PUBLISHED

* Active version
* Executable
* Immutable

### ARCHIVED

* Read-only
* Historical version
* Cannot receive new executions

---

## Workflow Instance Lifecycle

A workflow instance represents an actual execution.

### States

```text
CREATED
  |
READY
  |
RUNNING
  |
+-----------+
|           |
v           v
COMPLETED FAILED
      |
      v
CANCELLED
```

### CREATED

Workflow execution request received.

### READY

Execution prepared and waiting.

### RUNNING

Workflow actively executing.

### COMPLETED

Execution finished successfully.

### FAILED

Execution failed.

### CANCELLED

Execution terminated manually.

---

## Task Instance Lifecycle

```text
PENDING
   |
READY
   |
RUNNING
   |
+-----------+
|           |
v           v
COMPLETED FAILED
            |
            v
        RETRYING
```

### PENDING

Dependencies not satisfied.

### READY

Eligible for execution.

### RUNNING

Currently executing.

### COMPLETED

Successfully executed.

### FAILED

Execution failed.

### RETRYING

Waiting for retry attempt.
