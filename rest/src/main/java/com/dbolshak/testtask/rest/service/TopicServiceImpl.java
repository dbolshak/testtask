package com.dbolshak.testtask.rest.service;

import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.dao.TimeStampContent;
import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunDto;
import com.dbolshak.testtask.rest.dto.LastRunStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicDao fileSystemDao;

    public ExistingTopicsDto getAllExistingTopics() {
        ExistingTopicsDto result = new ExistingTopicsDto();
        result.setExistingTopics(fileSystemDao.findAllTopics());
        return result;
    }

    @Override
    public LastRunDto findLastRunFor(String topic) {
        LastRunDto lastRunDto = new LastRunDto(topic);
        lastRunDto.setLastRun(fileSystemDao.findLastRun(topic));
        return lastRunDto;
    }

    @Override
    public LastRunStatsDto getLastRunStats(String topic) {
        LastRunStatsDto lastRunStatsDto = new LastRunStatsDto(topic);
        TimeStampContent timeStampContent = fileSystemDao.findTimeStampContent(new TimeStamp(topic, fileSystemDao.findLastRun(topic)));
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
        TimeStampContent timeStampContent = fileSystemDao.findTimeStampContent(new TimeStamp(topic, fileSystemDao.findLastRun(topic)));
        lastRunDetailsDto.setMessagesForPartition(timeStampContent.getContent());
        return lastRunDetailsDto;
    }

    @Override
    public boolean topicExists(String topic) {
        return fileSystemDao.topicExists(topic);
    }
}
