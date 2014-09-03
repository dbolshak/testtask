package com.dbolshak.testtask.rest.service;

import com.dbolshak.testtask.model.dto.ExistingTopicsDto;
import com.dbolshak.testtask.model.dto.LastRunningDetailsDto;
import com.dbolshak.testtask.model.dto.LastRunningDto;
import com.dbolshak.testtask.model.dto.StatisticsForLastRunningDto;

/**
 * Created by dbolshak on 03.09.2014.
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
