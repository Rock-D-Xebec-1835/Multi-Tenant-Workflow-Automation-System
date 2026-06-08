# Workflow JSON Schema

## Overview

Workflows are stored as versioned JSON documents.

Each workflow consists of:

* Metadata
* Nodes
* Edges

The workflow must represent a Directed Acyclic Graph (DAG).

---

## Workflow Structure

```json
{
  "workflowId": "purchase-approval",
  "name": "Purchase Approval",
  "version": 1,
  "nodes": [],
  "edges": []
}
```

---

## Node Structure

```json
{
  "id": "managerApproval",
  "name": "Manager Approval",
  "type": "MANUAL_TASK",
  "config": {}
}
```

### Node Fields

| Field  | Description                 |
| ------ | --------------------------- |
| id     | Unique node identifier      |
| name   | Human readable name         |
| type   | Task type                   |
| config | Task-specific configuration |

---

## Supported Node Types

### START

Entry point of workflow.

### END

Terminal node of workflow.

### MANUAL_TASK

Human approval or action.

Example:

```json
{
  "assigneeRole": "MANAGER"
}
```

### HTTP_TASK

Calls external APIs.

Example:

```json
{
  "method": "POST",
  "url": "https://api.example.com"
}
```

### CONDITION_TASK

Branching logic.

Example:

```json
{
  "expression": "amount > 10000"
}
```

### DELAY_TASK

Wait before continuing.

Example:

```json
{
  "duration": "PT24H"
}
```

### WEBHOOK_TASK

Send outbound event.

Example:

```json
{
  "url": "https://client-system.com/webhook"
}
```

---

## Edge Structure

```json
{
  "from": "managerApproval",
  "to": "financeApproval"
}
```

### Conditional Edge

```json
{
  "from": "amountCheck",
  "to": "financeApproval",
  "condition": "TRUE"
}
```
