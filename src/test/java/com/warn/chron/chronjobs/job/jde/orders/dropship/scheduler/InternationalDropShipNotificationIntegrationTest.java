package com.warn.chron.chronjobs.job.jde.orders.dropship.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@SpringBootTest
@RunWith(SpringRunner.class)
public class InternationalDropShipNotificationIntegrationTest {

    @Autowired
    public InternationalDropShipNotificationMonitor internationalDropShipNotificationMonitor;

    @Test
    public void executeTest() {
        internationalDropShipNotificationMonitor.execute();
    }

}
