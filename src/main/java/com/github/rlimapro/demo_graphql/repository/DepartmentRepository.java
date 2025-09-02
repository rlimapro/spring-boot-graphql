package com.github.rlimapro.demo_graphql.repository;

import com.github.rlimapro.demo_graphql.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByManagerId(Long managerId);
    boolean existsByManagerIdAndIdNot(Long managerId, Long departmentId);
}
