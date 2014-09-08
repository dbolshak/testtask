package com.dbolshak.testtask.rest.service;

import com.dbolshak.testtask.dao.TimeStampInfo;
import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDto;
import com.dbolshak.testtask.rest.dto.StatisticsForLastRunningDto;
import org.mockito.Mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class TopicServiceImplTest {
    @InjectMocks
    private TopicService service = new TopicServiceImpl();
    @Mock
    private TopicDao topicDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllExistingTopics() throws Exception {
        when(topicDao.findAllTopics()).thenReturn(new ArrayList<String>() {{
            add("topic-1");
            add("topic-2");
        }});
        ExistingTopicsDto existingTopicsDto = service.getAllExistingTopics();

        assertEquals(2, existingTopicsDto.getExistingTopics().size());
    }

    @Test
    public void testFindLastRunningFor() throws Exception {
        String topic1 = "topic-1";
        String timeStamp = "1984-19-12-00-00-00";
        when(topicDao.findLastRunningFor(topic1)).thenReturn(timeStamp);

        LastRunningDto lastRunningDto = service.findLastRunningFor(topic1);

        assertEquals(timeStamp, lastRunningDto.getLastRunning());

    }

    @Test
    public void testGetStaticsForLastRunningByTopic() throws Exception {
        String topic1 = "topic-1";
        String timeStamp = "1984-19-12-00-00-00";
        TimeStampInfo timeStampInfo = new TimeStampInfo();
        timeStampInfo.put(1, 1l);
        timeStampInfo.put(2, 3l);
        when(topicDao.findLastRunningFor(topic1)).thenReturn(timeStamp);
        when(topicDao.findTimeStampInfo(topic1, timeStamp)).thenReturn(timeStampInfo);
        StatisticsForLastRunningDto statisticsForLastRunningDto = service.getStaticsForLastRunningByTopic(topic1);

        assertEquals(1l, statisticsForLastRunningDto.getMin());
        assertEquals(3l, statisticsForLastRunningDto.getMax());
        assertEquals(BigDecimal.valueOf(4), statisticsForLastRunningDto.getTotal());
    }

    @Test
    public void testGetLastRunningDetailsByTopic() throws Exception {
        String topic1 = "topic-1";
        String timeStamp = "1984-19-12-00-00-00";
        TimeStampInfo timeStampInfo = new TimeStampInfo();
        timeStampInfo.put(1, 1l);
        timeStampInfo.put(2, 3l);

        when(topicDao.findLastRunningFor(topic1)).thenReturn(timeStamp);
        when(topicDao.findTimeStampInfo(topic1, timeStamp)).thenReturn(timeStampInfo);
        LastRunningDetailsDto lastRunningDetailsDto = service.getLastRunningDetailsByTopic(topic1);

        assertEquals(2, lastRunningDetailsDto.getMessagesForPartition().size());
    }

    @Test
    public void testTopicExists() throws Exception {
        String topic1 = "topic-1";
        when(topicDao.topicExists(topic1)).thenReturn(true);

        assertTrue(service.topicExists(topic1));
    }
}