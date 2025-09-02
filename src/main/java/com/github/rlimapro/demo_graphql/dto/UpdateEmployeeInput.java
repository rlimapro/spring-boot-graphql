package com.github.rlimapro.demo_graphql.dto;

import java.math.BigDecimal;

public record UpdateEmployeeInput(
    String firstName,
    String lastName,
    BigDecimal salary,
    Long departmentId,
    Long supervisorId
) {}
