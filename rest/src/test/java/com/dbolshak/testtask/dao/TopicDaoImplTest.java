package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.dao.cache.CacheService;
import com.dbolshak.testtask.rest.service.TopicService;
import com.dbolshak.testtask.rest.service.TopicServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class TopicDaoImplTest {
    @InjectMocks
    private TopicDao service = new TopicDaoImpl();
    @Mock
    private CacheService cacheService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllTopics() throws Exception {
        String topic = "topic1";
        String timeStamp = "timeStamp";
        service.addTimeStamp(timeStamp, topic);

        assertEquals(topic, service.findAllTopics().iterator().next());
        assertEquals(1, service.findAllTopics().size());
    }

    @Test
    public void testClear() throws Exception {
        String topic = "topic1";
        String timeStamp = "timeStamp";
        service.addTimeStamp(timeStamp, topic);

        assertEquals(topic, service.findAllTopics().iterator().next());
        assertEquals(1, service.findAllTopics().size());

        service.clear();
        assertEquals(0, service.findAllTopics().size());
    }

    @Test
    public void testRemoveTimeStamp() throws Exception {
        String topic = "topic1";
        String timeStamp = "timeStamp";
        service.addTimeStamp(timeStamp, topic);

        assertEquals(timeStamp, service.findLastRunningFor(topic));

        service.removeTimeStamp(timeStamp, topic);
        assertEquals("", service.findLastRunningFor(topic));
    }

    @Test
    public void testAddTimeStamp() throws Exception {
        assertEquals(0, service.findAllTopics().size());

        String topic = "topic1";
        String timeStamp = "timeStamp";
        service.addTimeStamp(timeStamp, topic);

        assertEquals(topic, service.findAllTopics().iterator().next());
        assertEquals(1, service.findAllTopics().size());
    }

    @Test
    public void testFindLastRunningFor() throws Exception {
        String topic = "topic1";
        String timeStamp1 = "1984-19-12-00-00-01";
        String timeStamp2 = "1984-19-12-00-00-00";
        service.addTimeStamp(timeStamp1, topic);
        service.addTimeStamp(timeStamp2, topic);

        assertEquals(timeStamp1, service.findLastRunningFor(topic));

    }

    @Test
    public void testFindTimeStampInfo() throws Exception {
        String topic = "topic1";
        String timeStamp2 = "1984-19-12-00-00-00";
        String timeStamp1 = "1984-19-12-00-00-01";
        service.addTimeStamp(timeStamp1, topic);
        service.addTimeStamp(timeStamp2, topic);

        TimeStampInfo timeStampInfo = new TimeStampInfo();
        timeStampInfo.put(1, 1l);
        timeStampInfo.put(2, 3l);

        when(cacheService.get(topic, timeStamp1)).thenReturn(timeStampInfo);

        assertEquals(timeStampInfo, service.findTimeStampInfo(topic, service.findLastRunningFor(topic)));
    }

    @Test
    public void testTopicExists() throws Exception {
        String topic = "topic1";
        String timeStamp = "1984-19-12-00-00-00";
        service.addTimeStamp(timeStamp, topic);

        assertEquals(true, service.topicExists(topic));
    }
}