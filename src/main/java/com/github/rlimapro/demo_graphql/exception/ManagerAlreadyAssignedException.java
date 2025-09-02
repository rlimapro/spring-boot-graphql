package com.github.rlimapro.demo_graphql.exception;

public class ManagerAlreadyAssignedException extends RuntimeException {
    public ManagerAlreadyAssignedException(String message) {
        super(message);
    }
}
