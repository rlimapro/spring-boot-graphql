package com.github.rlimapro.demo_graphql.dto;

public record UpdateDepartmentInput(
    String depName,
    Long managerId
) {}
