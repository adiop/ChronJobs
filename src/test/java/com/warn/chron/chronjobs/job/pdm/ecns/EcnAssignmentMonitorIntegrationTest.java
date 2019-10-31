package com.warn.chron.chronjobs.job.pdm.ecns;

import com.warn.chron.chronjobs.job.pdm.ecns.scheduler.EcnAssignmentMonitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EcnAssignmentMonitorIntegrationTest {

    @Autowired
    EcnAssignmentMonitor ecnAssignmentMonitor;

    @Test
    public void executeTest() {
        ecnAssignmentMonitor.execute();
    }

}
