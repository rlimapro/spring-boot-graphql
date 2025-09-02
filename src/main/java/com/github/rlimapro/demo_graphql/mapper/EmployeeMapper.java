package com.github.rlimapro.demo_graphql.mapper;

import com.github.rlimapro.demo_graphql.domain.Department;
import com.github.rlimapro.demo_graphql.domain.Employee;
import com.github.rlimapro.demo_graphql.dto.CreateEmployeeInput;
import com.github.rlimapro.demo_graphql.dto.UpdateEmployeeInput;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", source = "department")
    @Mapping(target = "supervisor", source = "supervisor")
    @Mapping(target = "firstName", source = "input.firstName")
    @Mapping(target = "lastName", source = "input.lastName")
    @Mapping(target = "salary", source = "input.salary")
    Employee toEntity(CreateEmployeeInput input, Department department, Employee supervisor);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "supervisor", ignore = true)
    void updateEmployeeFromInput(UpdateEmployeeInput input, @MappingTarget Employee employee);
}
