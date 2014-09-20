package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.dao.Computable;
import com.dbolshak.testtask.fs.FileSystemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.dbolshak.testtask.Fixture.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CacheServiceImplTest {
    @InjectMocks
    private CacheService service = new CacheServiceImpl();
    @Mock
    private Computable fileReader;
    @Mock
    private FileSystemService fileSystemService;

    @Test
    public void testRemove() throws Exception {
        String timeStamp = "some csv file";
        String topic = "topic";
        String fullPath = topic + timeStamp;

        when(fileSystemService.getAbsoluteFileName(new TimeStamp(topic, timeStamp))).thenReturn(fullPath);

        when(fileReader.compute(fullPath)).thenReturn(TIME_STAMP_CONTENT);

        assertEquals(TIME_STAMP_CONTENT, service.get(new TimeStamp(topic, timeStamp)));
        verify(fileReader).compute(fullPath);

        assertEquals(TIME_STAMP_CONTENT, service.get(new TimeStamp(topic, timeStamp)));
        verifyZeroInteractions(fileReader);

        when(fileSystemService.getAbsoluteFileName(new TimeStamp(topic, timeStamp))).thenReturn(fullPath);
        service.remove(new TimeStamp(topic, timeStamp));
        service.get(new TimeStamp(topic, timeStamp));
        verify(fileReader, atLeast(2)).compute(fullPath);//init cache again
    }

    @Test
    public void testGet() throws Exception {
        String fileName = "some csv file";

        when(fileReader.compute(fileName)).thenReturn(TIME_STAMP_CONTENT);
        assertEquals(TIME_STAMP_CONTENT, service.get(fileName));
        verify(fileReader).compute(fileName);

        assertEquals(TIME_STAMP_CONTENT, service.get(fileName));
        verifyZeroInteractions(fileReader);
    }

    @Test
    public void testGet1() throws Exception {
        String timeStamp = "some csv file";
        String topic = "topic";
        String fullPath = topic + timeStamp;

        when(fileSystemService.getAbsoluteFileName(new TimeStamp(topic, timeStamp))).thenReturn(fullPath);

        when(fileReader.compute(fullPath)).thenReturn(TIME_STAMP_CONTENT);

        assertEquals(TIME_STAMP_CONTENT, service.get(new TimeStamp(topic, timeStamp)));
        verify(fileReader).compute(fullPath);
    }
}