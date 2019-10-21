package com.warn.chron.chronjobs.job.jde.orders.dropship.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

public class DomesticDropShipNotificationMonitor {
//    @Scheduled(fixedRate = 1000000)
    @Scheduled(cron = "0 0 21 * * ?")
    public void execute() {

    }

}
