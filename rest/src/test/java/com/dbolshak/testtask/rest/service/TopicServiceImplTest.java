package com.dbolshak.testtask.rest.service;

import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunDto;
import com.dbolshak.testtask.rest.dto.LastRunStatsDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.dbolshak.testtask.Fixture.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TopicServiceImplTest {
    @InjectMocks
    private TopicService service = new TopicServiceImpl();
    @Mock
    private TopicDao topicCacheDao;

    @Before
    public void createFixture() {
        when(topicCacheDao.findAllTopics()).thenReturn(ALL_TOPICS);
        when(topicCacheDao.topicExists(TOPIC)).thenReturn(true);
        when(topicCacheDao.findLastRun(TOPIC)).thenReturn(LAST_RUN);
        when(topicCacheDao.findTimeStampContent(TIME_STAMP)).thenReturn(TIME_STAMP_CONTENT);
    }

    @Test
    public void testGetAllExistingTopics() throws Exception {
        ExistingTopicsDto existingTopicsDto = service.getAllExistingTopics();

        assertEquals(ALL_TOPICS.size(), existingTopicsDto.getExistingTopics().size());
    }

    @Test
    public void testFindLastRun() throws Exception {
        LastRunDto lastRunDto = service.findLastRunFor(TOPIC);

        assertEquals(LAST_RUN, lastRunDto.getLastRun());
    }

    @Test
    public void testGetLastRunStats() throws Exception {
        LastRunStatsDto lastRunStatsDto = service.getLastRunStats(TOPIC);

        assertEquals(1l, lastRunStatsDto.getMin());
        assertEquals(3l, lastRunStatsDto.getMax());
        assertEquals(Double.valueOf(2), Double.valueOf(lastRunStatsDto.getAverage()));
        assertEquals(BigDecimal.valueOf(4), lastRunStatsDto.getTotal());
    }

    @Test
    public void testGetLastRunDetails() throws Exception {
        LastRunDetailsDto lastRunDetailsDto = service.getLastRunDetails(TOPIC);

        assertEquals(2, lastRunDetailsDto.getMessagesForPartition().size());
    }

    @Test
    public void ifTopicDaoHasTopicThenServiceMustReturnTrue() throws Exception {
        assertTrue(service.topicExists(TOPIC));
    }
}
