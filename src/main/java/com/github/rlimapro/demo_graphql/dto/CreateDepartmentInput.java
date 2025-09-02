package com.github.rlimapro.demo_graphql.dto;

public record CreateDepartmentInput(
    String depName,
    Long managerId
) {}
