package com.warn.chron.chronjobs.job.pdm.masterlog;

import com.warn.chron.chronjobs.job.pdm.masterlog.scheduler.MasterlogDrawings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MasterLogDrawingIntegrationTest {

    @Autowired
    public MasterlogDrawings masterlogDrawings;

    @Test
    public void executeTest() {
        masterlogDrawings.execute();
    }
}
