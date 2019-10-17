package com.warn.chron.chronjobs.job.jde.orders.dropship.scheduler;

import com.warn.chron.chronjobs.job.jde.orders.dropship.business.DropShipNotificationService;
import com.warn.chron.chronjobs.job.jde.orders.dropship.entities.international.DropShipInternational;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DropShipNotificationServiceIntegrationTest {

    @Autowired
    private DropShipNotificationService dropShipNotificationService;

    @Test
    public void sendInternationalDropShipNotificationTest() {

        List<DropShipInternational> dropShipInternationalList = IntStream.range(0, 1)
                                                                        .mapToObj(i -> {

                                                                            DropShipInternational dropShipInternational = new DropShipInternational("72630",
                                                                                    "A B Rindab (X) ",
                                                                                    "373291",
                                                                                    "SD",
                                                                                    "78100 ",
                                                                                    "30",
                                                                                    "10/14/19",
                                                                                    "10/14/19",
                                                                                    "INTL Ocean Freight ");

                                                                            return dropShipInternational;

                                                                        }).collect(Collectors.toList());

        String subject = "Reminder Scheduled Shipment Past Due International Drop Ship Notification";

        dropShipNotificationService.sendInternationalDropShipNotification(dropShipInternationalList, subject);
    }

}
