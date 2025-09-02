package com.github.rlimapro.demo_graphql.mapper;

import com.github.rlimapro.demo_graphql.domain.Department;
import com.github.rlimapro.demo_graphql.domain.Employee;
import com.github.rlimapro.demo_graphql.dto.CreateDepartmentInput;
import com.github.rlimapro.demo_graphql.dto.UpdateDepartmentInput;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manager", source = "manager")
    Department toEntity(CreateDepartmentInput input, Employee manager);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "employees", ignore = true)
    void updateDepartmentFromInput(UpdateDepartmentInput input, @MappingTarget Department department);
}
