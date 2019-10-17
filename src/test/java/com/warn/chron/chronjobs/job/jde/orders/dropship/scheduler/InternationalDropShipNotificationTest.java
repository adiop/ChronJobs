package com.warn.chron.chronjobs.job.jde.orders.dropship.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.application.common.DatabaseInformation;
import com.warn.chron.chronjobs.job.jde.orders.dropship.business.DropShipNotificationService;
import com.warn.chron.chronjobs.job.jde.orders.dropship.entities.international.DropShipInternational;
import com.warn.chron.chronjobs.job.jde.orders.dropship.scheduler.InternationalDropShipNotificationMonitor;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InternationalDropShipNotificationTest {

    public InternationalDropShipNotificationMonitor internationalDropShipNotificationMonitor;

    @Before
    public void setUp() {

        ApplicationEnvironmentConfig dbApplicationConfig = mock(ApplicationEnvironmentConfig.class);
        JdbcTemplate jdeBusinessJdbcTemplate = mock(JdbcTemplate.class);
        DropShipNotificationService dropShipNotificationService = mock(DropShipNotificationService.class);

        when(dbApplicationConfig.getJdeBusiness())
                .thenReturn(new DatabaseInformation() {

                    @Override
                    public String getDb() {
                        return "CRPDTA";
                    }
                });

//        doNothing()
//                .when(dropShipNotificationService)
//                .sendInternationalDropShipNotification(anyList(), anyString());

        doAnswer((invocation) -> {

            List<DropShipInternational> dropShipInternationalList = (List<DropShipInternational>) invocation.getArgument(0);
            String subject = (String) invocation.getArgument(1);

            System.out.println("###########");
            System.out.println(dropShipInternationalList);
            System.out.println(subject);

            return null;
        }).when(dropShipNotificationService)
                .sendInternationalDropShipNotification(anyList(), anyString());

        internationalDropShipNotificationMonitor = new InternationalDropShipNotificationMonitor(dbApplicationConfig, jdeBusinessJdbcTemplate, dropShipNotificationService);

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

    @Test
    public void testExecute() {
        internationalDropShipNotificationMonitor.execute();
    }





}
