package com.github.rlimapro.demo_graphql.dto;

import java.math.BigDecimal;

public record CreateEmployeeInput(
    String firstName,
    String lastName,
    BigDecimal salary,
    Long departmentId,
    Long supervisorId
) {}
