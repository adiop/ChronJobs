package com.warn.chron.chronjobs.job.jde.orders.releasereport.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderReleaseReportNotificationIntegrationTest {

    @Autowired
    public OrderReleaseReportNotificationMonitor orderReleaseReportNotificationMonitor;

    @Test
    public void executeTest() {
        orderReleaseReportNotificationMonitor.execute();
    }

}
