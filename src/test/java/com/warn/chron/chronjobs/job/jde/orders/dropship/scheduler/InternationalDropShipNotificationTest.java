package com.warn.chron.chronjobs.job.jde.orders.dropship.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.application.common.DatabaseInformation;
import com.warn.chron.chronjobs.job.jde.orders.dropship.scheduler.InternationalDropShipNotificationMonitor;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InternationalDropShipNotificationTest {

    public InternationalDropShipNotificationMonitor internationalDropShipNotificationMonitor;

    @Before
    public void setUp() {

        ApplicationEnvironmentConfig dbApplicationConfig = mock(ApplicationEnvironmentConfig.class);
        JdbcTemplate jdeBusinessJdbcTemplate = mock(JdbcTemplate.class);

        when(dbApplicationConfig.getJdeBusiness())
                .thenReturn(new DatabaseInformation() {

                    @Override
                    public String getDb() {
                        return "CRPDTA";
                    }
                });

        internationalDropShipNotificationMonitor = new InternationalDropShipNotificationMonitor(dbApplicationConfig, jdeBusinessJdbcTemplate);

    }

    @Test
    public void todayQueryTest() {
        System.out.println("International Drop Ship Notification");
        System.out.println(internationalDropShipNotificationMonitor.getOrdersToBeShippedTodayQuery());
    }

    @Test
    public void nextWeekQueryTest() {
        System.out.println("Reminder Scheduled Shipment Pending International Drop Ship Notification");
        System.out.println(internationalDropShipNotificationMonitor.getOrdersToBePickedIn7Days());
    }

    @Test
    public void yesterdayQueryTest() {
        System.out.println("Reminder Scheduled Shipment Past Due International Drop Ship Notification");
        System.out.println(internationalDropShipNotificationMonitor.getOrdersToBePickedYesterday());
    }





}
