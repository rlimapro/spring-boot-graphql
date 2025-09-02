package com.github.rlimapro.demo_graphql.service;

import com.github.rlimapro.demo_graphql.domain.Department;
import com.github.rlimapro.demo_graphql.domain.Employee;
import com.github.rlimapro.demo_graphql.dto.CreateDepartmentInput;
import com.github.rlimapro.demo_graphql.dto.UpdateDepartmentInput;
import com.github.rlimapro.demo_graphql.exception.ManagerAlreadyAssignedException;
import com.github.rlimapro.demo_graphql.exception.ResourceNotFoundException;
import com.github.rlimapro.demo_graphql.mapper.DepartmentMapper;
import com.github.rlimapro.demo_graphql.repository.DepartmentRepository;
import com.github.rlimapro.demo_graphql.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;

    public Department findById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department createDepartment(CreateDepartmentInput input) {
        Employee manager = null;

        if (input.managerId() != null) {

            if (departmentRepository.existsByManagerId(input.managerId())) {
                throw new ManagerAlreadyAssignedException("This employee is already a manager in another department.");
            }

            manager = employeeRepository.findById(input.managerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager with ID " + input.managerId() + " not found."));
        }

        Department entity = departmentMapper.toEntity(input, manager);

        return departmentRepository.save(entity);
    }

    public Department updateDepartment(Long id, UpdateDepartmentInput input) {
        Department departmentToUpdate = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department with ID " + id + " not found."));

        departmentMapper.updateDepartmentFromInput(input, departmentToUpdate);

        if(input.managerId() != null) {

            if (departmentRepository.existsByManagerIdAndIdNot(input.managerId(), id)) {
                throw new ManagerAlreadyAssignedException("This employee is already a manager in another department.");
            }

            Employee newManager = employeeRepository.findById(input.managerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager with ID: " + input.managerId() + "not found."));
            departmentToUpdate.setManager(newManager);
        }

        return departmentRepository.save(departmentToUpdate);
    }

    public Map<Department, Employee> getManagersForDepartments(List<Department> departments) {
        // Get all manager IDs from department list
        Set<Long> managerIds = departments.stream()
            .filter(department -> department.getManager() != null)
            .map(department -> department.getManager().getId())
            .collect(Collectors.toSet());

        // If no department has a manager, return an empty map
        if (managerIds.isEmpty()) {
            return Map.of();
        }

        // Call the service once to search for all managers
        Map<Long, Employee> managersById = employeeRepository.findAllById(managerIds).stream()
            .collect(Collectors.toMap(Employee::getId, Function.identity()));

        // Map each manager to their respective department
        return departments.stream()
            .filter(department -> department.getManager() != null)
            .collect(Collectors.toMap(
                department -> department,
                department -> managersById.get(department.getManager().getId())
            ));
    }

    public Map<Department, List<Employee>> getEmployeesForDepartments(List<Department> departments) {
        // // Get all department IDs from departments list
        Set<Long> departmentIds = departments.stream()
            .map(Department::getId)
            .collect(Collectors.toSet());

        // Call the service once to search for all employees of all departments
        List<Employee> allEmployees = employeeRepository.findAllByDepartmentIdIn(departmentIds);

        // Groups the list of employees by the department ID they belong to.
        // The result is a map where the key is the department ID and the value is the list of employees
        Map<Long, List<Employee>> employeesByDepartmentId = allEmployees.stream()
            .collect(Collectors.groupingBy(employee -> employee.getDepartment().getId()));

        // Maps each department to its corresponding employee list
        return departments.stream()
            .collect(Collectors.toMap(
                department -> department,
                // Use getOrDefault to return an empty list if a department has no employees
                department -> employeesByDepartmentId.getOrDefault(department.getId(), Collections.emptyList())
            ));
    }
}
