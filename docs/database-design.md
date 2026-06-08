# Database Design

## Overview

The initial version of FlowForge focuses on workflow definition management.

The database is responsible for storing:

* Workflow Definitions
* Workflow Versions
* Workflow JSON DSL
* Version Lifecycle Information

Workflow execution data will be introduced in later phases.

---

# Database Technology

Database: MySQL 8+

Storage Engine: InnoDB

Character Set: utf8mb4

---

# Design Principles

## Versioned Workflows

Workflow definitions are immutable once published.

Every modification creates a new workflow version.

---

## JSON-Based Workflow Storage

Workflow nodes and edges are stored as JSON documents.

This avoids maintaining separate node and edge tables and simplifies workflow versioning.

---

## Tenant Isolation

Current phase uses a tenant_id column.

Full multi-tenant enforcement will be implemented in a later phase.

---

# Entity Relationship Diagram

```text
WorkflowDefinition
        |
        | 1 : N
        |
WorkflowVersion
```

---

# Table: workflow_definition

Represents a logical workflow.

Example:

* Purchase Approval
* Employee Onboarding
* Incident Escalation

A workflow definition may have multiple versions.

---

## Columns

| Column         | Type         | Description                 |
| -------------- | ------------ | --------------------------- |
| id             | BIGINT       | Primary Key                 |
| tenant_id      | BIGINT       | Owning tenant               |
| name           | VARCHAR(255) | Workflow name               |
| description    | TEXT         | Workflow description        |
| active_version | INT          | Currently published version |
| created_at     | TIMESTAMP    | Creation timestamp          |
| updated_at     | TIMESTAMP    | Last update timestamp       |

---

## Constraints

### Primary Key

```sql
PRIMARY KEY(id)
```

---

## Indexes

### Tenant Lookup

```sql
INDEX idx_workflow_tenant (tenant_id)
```

### Workflow Name Lookup

```sql
INDEX idx_workflow_name (name)
```

---

# Table: workflow_version

Represents a specific version of a workflow.

Example:

Purchase Approval

* Version 1
* Version 2
* Version 3

Each version contains a complete workflow definition JSON.

---

## Columns

| Column                 | Type        | Description                  |
| ---------------------- | ----------- | ---------------------------- |
| id                     | BIGINT      | Primary Key                  |
| workflow_definition_id | BIGINT      | Parent workflow              |
| version                | INT         | Version number               |
| status                 | VARCHAR(20) | DRAFT / PUBLISHED / ARCHIVED |
| definition_json        | JSON        | Workflow DSL                 |
| created_at             | TIMESTAMP   | Creation timestamp           |

---

## Constraints

### Primary Key

```sql
PRIMARY KEY(id)
```

### Foreign Key

```sql
FOREIGN KEY(workflow_definition_id)
REFERENCES workflow_definition(id)
```

### Unique Version Constraint

Each workflow may contain only one version with a given version number.

```sql
UNIQUE(workflow_definition_id, version)
```

---

## Indexes

### Workflow Lookup

```sql
INDEX idx_workflow_version_workflow
(workflow_definition_id)
```

### Status Lookup

```sql
INDEX idx_workflow_version_status
(status)
```

---

# Workflow Status

## DRAFT

Editable.

Cannot be executed.

---

## PUBLISHED

Active version.

Executable.

Immutable.

---

## ARCHIVED

Historical version.

Read-only.

Cannot receive new executions.

---

# Example WorkflowDefinition Record

```text
id: 1

tenant_id: 1

name: Purchase Approval

description: Purchase approval process

active_version: 2
```

---

# Example WorkflowVersion Record

```text
id: 12

workflow_definition_id: 1

version: 2

status: PUBLISHED
```

---

# Example Workflow JSON

```json
{
  "workflowId": "purchase-approval",
  "name": "Purchase Approval",
  "version": 2,

  "nodes": [
    {
      "id": "start",
      "type": "START"
    },
    {
      "id": "managerApproval",
      "type": "MANUAL_TASK"
    },
    {
      "id": "end",
      "type": "END"
    }
  ],

  "edges": [
    {
      "from": "start",
      "to": "managerApproval"
    },
    {
      "from": "managerApproval",
      "to": "end"
    }
  ]
}
```

---

# Future Tables (Out of Scope)

The following tables will be introduced in later phases:

## workflow_instance

Stores workflow executions.

## task_instance

Stores task executions.

## tenant

Stores organizations.

## user

Stores platform users.

## usage_record

Stores metering information.

## invoice

Stores billing information.
