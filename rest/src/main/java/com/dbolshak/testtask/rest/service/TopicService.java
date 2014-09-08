package com.dbolshak.testtask.rest.service;

import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDto;
import com.dbolshak.testtask.rest.dto.StatisticsForLastRunningDto;

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

    LastRunningDto findLastRunningFor(String topic);

    StatisticsForLastRunningDto getStaticsForLastRunningByTopic(String topic);

    LastRunningDetailsDto getLastRunningDetailsByTopic(String topic);
}
