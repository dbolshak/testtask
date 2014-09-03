package com.dbolshak.testtask.model.dto;

import java.util.Collection;

/**
 * Created by dbolshak on 03.09.2014.
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
