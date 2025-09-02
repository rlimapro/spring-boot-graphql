package com.github.rlimapro.demo_graphql.controller;

import com.github.rlimapro.demo_graphql.domain.Department;
import com.github.rlimapro.demo_graphql.domain.Employee;
import com.github.rlimapro.demo_graphql.dto.CreateDepartmentInput;
import com.github.rlimapro.demo_graphql.dto.UpdateDepartmentInput;
import com.github.rlimapro.demo_graphql.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @QueryMapping
    public Department department(@Argument Long id) {
        return departmentService.findById(id);
    }

    @QueryMapping
    public List<Department> allDepartments() {
        return departmentService.findAll();
    }

    @MutationMapping
    public Department createDepartment(@Argument CreateDepartmentInput input) {
        return departmentService.createDepartment(input);
    }

    @MutationMapping
    public Department updateDepartment(@Argument Long id, @Argument UpdateDepartmentInput input) {
        return departmentService.updateDepartment(id, input);
    }

    @BatchMapping(typeName = "Department", field = "manager")
    public Map<Department, Employee> getManagers(List<Department> departments) {
        return departmentService.getManagersForDepartments(departments);
    }

    @BatchMapping(typeName = "Department", field = "employees")
    public Map<Department, List<Employee>> getEmployees(List<Department> departments) {
        return departmentService.getEmployeesForDepartments(departments);
    }
}
