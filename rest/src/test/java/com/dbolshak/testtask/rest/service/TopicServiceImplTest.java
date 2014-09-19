package com.dbolshak.testtask.rest.service;

import com.dbolshak.testtask.dao.TimeStampContent;
import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDto;
import com.dbolshak.testtask.rest.dto.StatisticsForLastRunningDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TopicServiceImplTest {
    @InjectMocks
    private TopicService service = new TopicServiceImpl();
    @Mock
    private TopicDao topicDao;

    private static final String LAST_TOPIC = "topic-1";
    private static final List<String> ALL_TOPICS = Arrays.asList(LAST_TOPIC, "topic-2");
    private static final String TIME_STAMP_OF_LAST_TOPIC = "1984-19-12-00-00-00";

    @Before
    public void createFixture() {
        TimeStampContent timeStampContent = new TimeStampContent();
        timeStampContent.put(1, 1l);
        timeStampContent.put(2, 3l);

        when(topicDao.findAllTopics()).thenReturn(ALL_TOPICS);
        when(topicDao.topicExists(LAST_TOPIC)).thenReturn(true);
        when(topicDao.findLastRunningFor(LAST_TOPIC)).thenReturn(TIME_STAMP_OF_LAST_TOPIC);
        when(topicDao.findTimeStampContent(LAST_TOPIC, TIME_STAMP_OF_LAST_TOPIC)).thenReturn(timeStampContent);
    }

    @Test
    public void testGetAllExistingTopics() throws Exception {
        ExistingTopicsDto existingTopicsDto = service.getAllExistingTopics();

        assertEquals(ALL_TOPICS.size(), existingTopicsDto.getExistingTopics().size());
    }

    @Test
    public void testFindLastRunningFor() throws Exception {
        LastRunningDto lastRunningDto = service.findLastRunningFor(LAST_TOPIC);

        assertEquals(TIME_STAMP_OF_LAST_TOPIC, lastRunningDto.getLastRunning());
    }

    @Test
    public void testGetStaticsForLastRunningByTopic() throws Exception {
        StatisticsForLastRunningDto statisticsForLastRunningDto = service.getStaticsForLastRunningByTopic(LAST_TOPIC);

        assertEquals(1l, statisticsForLastRunningDto.getMin());
        assertEquals(3l, statisticsForLastRunningDto.getMax());
        assertEquals(BigDecimal.valueOf(4), statisticsForLastRunningDto.getTotal());
    }

    @Test
    public void testGetLastRunningDetailsByTopic() throws Exception {
        LastRunningDetailsDto lastRunningDetailsDto = service.getLastRunningDetailsByTopic(LAST_TOPIC);

        assertEquals(2, lastRunningDetailsDto.getMessagesForPartition().size());
    }

    @Test
    public void ifTopicDaoHasTopicThenServiceMustReturnTrue() throws Exception {
        assertTrue(service.topicExists(LAST_TOPIC));
    }
}
