package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.Computable;
import com.dbolshak.testtask.dao.TimeStampInfo;
import com.dbolshak.testtask.fs.FileSystemService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CacheServiceImplTest {
    @InjectMocks
    private CacheService service = new CacheServiceImpl();
    @Mock
    private Computable fileReader;
    @Mock
    private FileSystemService fileSystemService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRemove() throws Exception {
        String timeStamp = "some csv file";
        String topic = "topic";
        String fullPath = topic + timeStamp;

        when(fileSystemService.getAbsolutFileName(topic, timeStamp)).thenReturn(fullPath);

        TimeStampInfo timeStampInfo = new TimeStampInfo();
        timeStampInfo.put(1, 1l);
        timeStampInfo.put(2, 3l);
        when(fileReader.compute(fullPath)).thenReturn(timeStampInfo);

        assertEquals(timeStampInfo, service.get(topic, timeStamp));
        verify(fileReader).compute(fullPath);

        assertEquals(timeStampInfo, service.get(topic, timeStamp));
        verifyZeroInteractions(fileReader);

        when(fileSystemService.getAbsolutFileName(topic, timeStamp)).thenReturn(fullPath);
        service.remove(topic, timeStamp);
        service.get(topic, timeStamp);
        verify(fileReader, atLeast(2)).compute(fullPath);//init cache again
    }

    @Test
    public void testGet() throws Exception {
        String fileName = "some csv file";
        TimeStampInfo timeStampInfo = new TimeStampInfo();
        timeStampInfo.put(1, 1l);
        timeStampInfo.put(2, 3l);

        when(fileReader.compute(fileName)).thenReturn(timeStampInfo);
        assertEquals(timeStampInfo, service.get(fileName));
        verify(fileReader).compute(fileName);

        assertEquals(timeStampInfo, service.get(fileName));
        verifyZeroInteractions(fileReader);
    }

    @Test
    public void testGet1() throws Exception {
        String timeStamp = "some csv file";
        String topic = "topic";
        String fullPath = topic + timeStamp;

        when(fileSystemService.getAbsolutFileName(topic, timeStamp)).thenReturn(fullPath);

        TimeStampInfo timeStampInfo = new TimeStampInfo();
        timeStampInfo.put(1, 1l);
        timeStampInfo.put(2, 3l);
        when(fileReader.compute(fullPath)).thenReturn(timeStampInfo);

        assertEquals(timeStampInfo, service.get(topic, timeStamp));
        verify(fileReader).compute(fullPath);
    }
}