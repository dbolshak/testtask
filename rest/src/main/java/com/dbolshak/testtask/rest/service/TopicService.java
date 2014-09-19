package com.dbolshak.testtask.rest.service;

import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunDto;
import com.dbolshak.testtask.rest.dto.LastRunStatsDto;

/**
 * A middle layer service which must separate presentation and business logic.
 */
public interface TopicService {
    /**
     * @param topic which need to be checked for existence
     * @return true if such topic exists, otherwise false
     */
    boolean topicExists(String topic);

    /**
     * @return all existing topics (only their names)
     */
    ExistingTopicsDto getAllExistingTopics();

    LastRunDto findLastRunFor(String topic);

    LastRunStatsDto getLastRunStats(String topic);

    LastRunDetailsDto getLastRunDetails(String topic);
}
