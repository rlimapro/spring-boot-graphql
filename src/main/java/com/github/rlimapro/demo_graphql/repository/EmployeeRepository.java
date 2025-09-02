package com.github.rlimapro.demo_graphql.repository;

import com.github.rlimapro.demo_graphql.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByDepartmentIdIn(Set<Long> departmentIds);
}
