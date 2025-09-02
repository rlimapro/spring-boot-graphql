package com.github.rlimapro.demo_graphql.controller;

import com.github.rlimapro.demo_graphql.domain.Department;
import com.github.rlimapro.demo_graphql.domain.Employee;
import com.github.rlimapro.demo_graphql.dto.CreateEmployeeInput;
import com.github.rlimapro.demo_graphql.dto.EmployeePage;
import com.github.rlimapro.demo_graphql.dto.UpdateEmployeeInput;
import com.github.rlimapro.demo_graphql.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @QueryMapping
    public Employee employee(@Argument Long id) {
        return employeeService.findById(id);
    }

    @QueryMapping
    public EmployeePage allEmployees(@Argument Integer page, @Argument Integer size, @Argument String direction) {
        Sort.Direction dir = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, "firstName"));
        return employeeService.findAll(pageable);
    }

    @MutationMapping
    public Employee createEmployee(@Argument CreateEmployeeInput input) {
        return employeeService.createEmployee(input);
    }

    @MutationMapping
    public Employee updateEmployee(@Argument Long id, @Argument UpdateEmployeeInput input) {
        return employeeService.updateEmployee(id, input);
    }

    @BatchMapping(typeName = "Employee", field = "department")
    public Map<Employee, Department> getDepartments(List<Employee> employees) {
        return employeeService.getDepartmentsForEmployees(employees);
    }

    @BatchMapping(typeName = "Employee", field = "supervisor")
    public Map<Employee, Employee> getSupervisors(List<Employee> employees) {
        return employeeService.getSupervisorsForEmployees(employees);
    }
}
