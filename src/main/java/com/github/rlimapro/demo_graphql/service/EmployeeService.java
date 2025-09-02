package com.github.rlimapro.demo_graphql.service;

import com.github.rlimapro.demo_graphql.domain.Department;
import com.github.rlimapro.demo_graphql.domain.Employee;
import com.github.rlimapro.demo_graphql.dto.CreateEmployeeInput;
import com.github.rlimapro.demo_graphql.dto.EmployeePage;
import com.github.rlimapro.demo_graphql.dto.UpdateEmployeeInput;
import com.github.rlimapro.demo_graphql.exception.ResourceNotFoundException;
import com.github.rlimapro.demo_graphql.mapper.EmployeeMapper;
import com.github.rlimapro.demo_graphql.repository.DepartmentRepository;
import com.github.rlimapro.demo_graphql.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public EmployeePage findAll(Pageable pageable) {
        var employees = employeeRepository.findAll(pageable);
        return new EmployeePage(employees);
    }

    public Employee createEmployee(CreateEmployeeInput input) {
        Department department = departmentRepository.findById(input.departmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Department with ID " + input.departmentId() + " not found."));

        Employee supervisor = null;
        if (input.supervisorId() != null) {
            supervisor = employeeRepository.findById(input.supervisorId())
                .orElseThrow(() -> new ResourceNotFoundException("Supervisor with ID " + input.supervisorId() + " not found."));
        }

        Employee employeeEntity = employeeMapper.toEntity(input, department, supervisor);

        return employeeRepository.save(employeeEntity);
    }

    public Employee updateEmployee(Long id, UpdateEmployeeInput input) {
        Employee employeeToUpdate = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + id + " not found."));

        employeeMapper.updateEmployeeFromInput(input, employeeToUpdate);

        if (input.departmentId() != null) {
            Department newDepartment = departmentRepository.findById(input.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department with ID " + input.departmentId() + " not found."));
            employeeToUpdate.setDepartment(newDepartment);
        }

        if (input.supervisorId() != null) {
            if (input.supervisorId().equals(id)) {
                throw new IllegalArgumentException("An employee cannot be his own supervisor.");
            }
            Employee newSupervisor = employeeRepository.findById(input.supervisorId())
                .orElseThrow(() -> new ResourceNotFoundException("Supervisor with ID " + input.supervisorId() + " not found."));
            employeeToUpdate.setSupervisor(newSupervisor);
        }

        return employeeRepository.save(employeeToUpdate);
    }

    public Map<Employee, Department> getDepartmentsForEmployees(List<Employee> employees) {
        // Gets all department IDs from employee list
        Set<Long> departmentIds = employees.stream()
            .map(employee -> employee.getDepartment().getId())
            .collect(Collectors.toSet());

        // Call the service once to search for all departments
        Map<Long, Department> departmentsById = departmentRepository.findAllById(departmentIds).stream()
            .collect(Collectors.toMap(Department::getId, Function.identity()));

        // Maps each employee to their respective department
        return employees.stream()
            .collect(Collectors.toMap(
                employee -> employee,
                employee -> departmentsById.get(employee.getDepartment().getId())
            ));
    }

    public Map<Employee, Employee> getSupervisorsForEmployees(List<Employee> employees) {
        // Gets the IDs of all supervisors, ignoring null ones
        Set<Long> supervisorIds = employees.stream()
            .filter(employee -> employee.getSupervisor() != null)
            .map(employee -> employee.getSupervisor().getId())
            .collect(Collectors.toSet());

        // If no employee has a supervisor, return an empty map
        if (supervisorIds.isEmpty()) {
            return Map.of();
        }

        // Call the service once to search for all supervisors
        Map<Long, Employee> supervisorsById = employeeRepository.findAllById(supervisorIds).stream()
            .collect(Collectors.toMap(Employee::getId, Function.identity()));

        // Maps each employee to their respective supervisor
        return employees.stream()
            .filter(employee -> employee.getSupervisor() != null)
            .collect(Collectors.toMap(
                employee -> employee,
                employee -> supervisorsById.get(employee.getSupervisor().getId())
            ));
    }
}
