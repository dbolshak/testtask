package com.dbolshak.testtask.rest.service;

import com.dbolshak.testtask.dao.TimeStampInfo;
import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDto;
import com.dbolshak.testtask.rest.dto.StatisticsForLastRunningDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by dbolshak on 03.09.2014.
 */
@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicDao topicDao;

    public ExistingTopicsDto getAllExistingTopics() {
        ExistingTopicsDto result = new ExistingTopicsDto();
        result.setExistingTopics(topicDao.findAllTopics());
        return result;
    }

    @Override
    public LastRunningDto findLastRunningFor(String topic) {
        LastRunningDto lastRunningDto = new LastRunningDto(topic);
        lastRunningDto.setLastRunning(topicDao.findLastRunningFor(topic));
        return lastRunningDto;
    }

    @Override
    public StatisticsForLastRunningDto getStaticsForLastRunningByTopic(String topic) {
        StatisticsForLastRunningDto statisticsForLastRunningDto = new StatisticsForLastRunningDto(topic);
        TimeStampInfo timeStampInfo = topicDao.findTimeStampInfo(topic, topicDao.findLastRunningFor(topic));
        BigDecimal total = BigDecimal.ZERO;
        if (!timeStampInfo.getContent().isEmpty()) {
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            for (Long messageCount : timeStampInfo.getContent().values()) {
                if (min >= messageCount) {
                    min = messageCount;
                }
                if (max <= messageCount) {
                    max = messageCount;
                }
                total = total.add(BigDecimal.valueOf(messageCount));
            }
            statisticsForLastRunningDto.setMin(min);
            statisticsForLastRunningDto.setMax(max);
            statisticsForLastRunningDto.setAverage(total.divide(BigDecimal.valueOf(timeStampInfo.getContent().size())).doubleValue());
        }
        statisticsForLastRunningDto.setTotal(total);
        return statisticsForLastRunningDto;
    }

    @Override
    public LastRunningDetailsDto getLastRunningDetailsByTopic(String topic) {
        LastRunningDetailsDto detailsForLastRunning = new LastRunningDetailsDto(topic);
        TimeStampInfo timeStampInfo = topicDao.findTimeStampInfo(topic, topicDao.findLastRunningFor(topic));
        detailsForLastRunning.setMessagesForPartition(timeStampInfo.getContent());
        return detailsForLastRunning;
    }

    @Override
    public boolean topicExists(String topic) {
        return topicDao.topicExists(topic);
    }
}
