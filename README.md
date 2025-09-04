# GraphQL Demo Project

A Spring Boot GraphQL application demonstrating employee and department management with efficient data fetching using DataLoader patterns.

## Overview

This project implements a GraphQL API for managing employees and departments with the following features:

* Employee CRUD operations with pagination
* Department CRUD operations
* Hierarchical relationships (employees have supervisors, departments have managers)
* Efficient N+1 query resolution using `@BatchMapping`
* In-memory H2 database with pre-populated test data

## Tech Stack

* Java 21
* Spring Boot 3.5.5
* Spring GraphQL
* Spring Data JPA
* H2 Database (in-memory)
* MapStruct (entity mapping)
* Lombok (boilerplate reduction)

## Database Schema

The application uses an in-memory H2 database that is automatically created and populated on startup:

* **Employees**: Personal information, salary, department assignment, and supervisor relationships
* **Departments**: Department name, manager assignment, and associated employees
* **Data Population**: The `data.sql` file in `src/main/resources/` contains sample data with multiple employees and departments for testing

## Getting Started

### Prerequisites

* Java 21 or higher
* Maven 3.6+ (or use the included Maven wrapper)
* IDE of your choice (IntelliJ IDEA, Eclipse, VS Code)

### Running the Application

#### Option 1: Using an IDE

* Clone the repository
* Import the project into your IDE
* Run the main class `Startup`
* The application will start on `http://localhost:8080`

#### Option 2: Using Maven

```
# Clone the repository
git clone https://github.com/rlimapro/spring-boot-graphql.git
cd spring-boot-graphql

# Run with Maven 
mvn spring-boot:run
```

## Accessing the GraphQL Playground

Once the application is running, access GraphiQL at:

```
http://localhost:8080/graphiql
``` 

## H2 Database Console

You can inspect the database at `http://localhost:8080/h2-console` using below credentials:

``` 
Driver Class: org.h2.Driver
Database URL: jdbc:h2:mem:demo_graphql
Username: sa
Password: (leave empty)
``` 

## GraphQL API Examples

### Queries

#### Get All Departments

```
query {
  allDepartments {
    id
    depName
    manager {
      id
      firstName
      lastName
    }
    employees {
      id
      firstName
      lastName
      salary
    }
  }
}
```

#### Get Specific Department

```
query {
  department(id: 1) {
    id
    depName
    manager {
      firstName
      lastName
    }
    employees {
      firstName
      lastName
      salary
    }
  }
}
``` 

####  Get All Employees with Pagination

```
query {
  allEmployees(page: 0, size: 5, direction: "asc") {
    content {
      id
      firstName
      lastName
      salary
      department {
        depName
      }
      supervisor {
        firstName
        lastName
      }
    }
    pageNumber
    pageSize
    totalElements
    totalPages
    first
    last
  }
}
```

#### Get Specific Employee

```
query {
  employee(id: 1) {
    id
    firstName
    lastName
    salary
    department {
      id
      depName
    }
    supervisor {
      firstName
      lastName
    }
  }
}
```

### Mutations

#### Create Department

```
mutation {
  createDepartment(input: {
    depName: "New Department"
    managerId: 20
  }) {
    id
    depName
    manager {
      firstName
      lastName
    }
  }
}
```

#### Create Department Without Manager

```
mutation {
  createDepartment(input: {
    depName: "Independent Department"
  }) {
    id
    depName
    manager {
      firstName
      lastName
    }
  }
}
```

#### Update Department

```
mutation {
  updateDepartment(id: 1, input: {
    depName: "Updated Department Name"
    managerId: 2
  }) {
    id
    depName
    manager {
      firstName
      lastName
    }
  }
}
```

#### Create Employee

``` 
mutation {
  createEmployee(input: {
    firstName: "John"
    lastName: "Doe"
    salary: 75000.0
    departmentId: 1
    supervisorId: 2
  }) {
    id
    firstName
    lastName
    salary
    department {
      depName
    }
    supervisor {
      firstName
      lastName
    }
  }
}
```

#### Create Employee Without Supervisor

```
mutation {
  createEmployee(input: {
    firstName: "Jane"
    lastName: "Smith"
    salary: 80000.0
    departmentId: 1
  }) {
    id
    firstName
    lastName
    salary
    department {
      depName
    }
  }
}
```

#### Update Employee

```
mutation {
  updateEmployee(id: 1, input: {
    firstName: "Updated Name"
    salary: 85000.0
    departmentId: 2
    supervisorId: 3
  }) {
    id
    firstName
    lastName
    salary
    department {
      depName
    }
    supervisor {
      firstName
      lastName
    }
  }
}
``` 

## Key Features Demonstrated

### Efficient Data Fetching

The application uses `@BatchMapping` to solve the N+1 query problem:

* **Department.manager:** Batch loads managers for multiple departments
* **Department.employees:** Batch loads employees for multiple departments
* **Employee.department:** Batch loads departments for multiple employees
* **Employee.supervisor:** Batch loads supervisors for multiple employees

### Business Logic Validation

* Employees cannot be their own supervisor
* Managers cannot manage multiple departments
* Proper error handling for non-existent entities