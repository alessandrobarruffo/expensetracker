package com.abar.expensetracker.exception;

public class EntityNotFoundException extends RuntimeException {

    public <T> EntityNotFoundException(String entityName, T entityId) {
        super(entityName + " with id " + entityId + " not found");
    }


}
