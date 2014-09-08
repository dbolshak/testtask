package com.dbolshak.testtask.rest.dto;

import java.util.Collection;

/**
 * Simple DTO object which is a middle layer object between frontend (JSON) and backend (POJO)
 */
public class ExistingTopicsDto {
    private Collection<String> existingTopics;

    public Collection<String> getExistingTopics() {
        return existingTopics;
    }

    public void setExistingTopics(Collection<String> existingTopics) {
        this.existingTopics = existingTopics;
    }

}
