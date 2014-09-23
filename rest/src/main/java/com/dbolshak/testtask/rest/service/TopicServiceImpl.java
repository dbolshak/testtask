package com.dbolshak.testtask.rest.service;

import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.model.TimeStamp;
import com.dbolshak.testtask.model.TimeStampContent;
import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunDto;
import com.dbolshak.testtask.rest.dto.LastRunStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    @Qualifier("topicCacheDao")
    private TopicDao topicDao;

    public ExistingTopicsDto getAllExistingTopics() {
        ExistingTopicsDto result = new ExistingTopicsDto();
        result.setExistingTopics(topicDao.findAllTopics());
        return result;
    }

    @Override
    public LastRunDto findLastRunFor(String topic) {
        LastRunDto lastRunDto = new LastRunDto(topic);
        lastRunDto.setLastRun(topicDao.findLastRun(topic));
        return lastRunDto;
    }

    @Override
    public LastRunStatsDto getLastRunStats(String topic) {
        LastRunStatsDto lastRunStatsDto = new LastRunStatsDto(topic);
        TimeStampContent timeStampContent = topicDao.findTimeStampContent(new TimeStamp(topic, topicDao.findLastRun(topic)));
        BigDecimal total = BigDecimal.ZERO;
        if (!timeStampContent.getContent().isEmpty()) {
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            for (Long messageCount : timeStampContent.getContent().values()) {
                if (min >= messageCount) {
                    min = messageCount;
                }
                if (max <= messageCount) {
                    max = messageCount;
                }
                total = total.add(BigDecimal.valueOf(messageCount));
            }
            lastRunStatsDto.setMin(min);
            lastRunStatsDto.setMax(max);
            lastRunStatsDto.setAverage(total.divide(BigDecimal.valueOf(timeStampContent.getContent().size())).doubleValue());
        }
        lastRunStatsDto.setTotal(total);
        return lastRunStatsDto;
    }

    @Override
    public LastRunDetailsDto getLastRunDetails(String topic) {
        LastRunDetailsDto lastRunDetailsDto = new LastRunDetailsDto(topic);
        TimeStampContent timeStampContent = topicDao.findTimeStampContent(new TimeStamp(topic, topicDao.findLastRun(topic)));
        lastRunDetailsDto.setMessagesForPartition(timeStampContent.getContent());
        return lastRunDetailsDto;
    }

    @Override
    public boolean topicExists(String topic) {
        return topicDao.topicExists(topic);
    }
}
