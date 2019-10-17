package com.warn.chron.chronjobs.job.jde.orders.dropship.business;

import com.warn.chron.chronjobs.business.notification.NotificationService;
import com.warn.chron.chronjobs.job.jde.orders.dropship.entities.international.DropShipInternational;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DropShipNotificationService {
    private static final String NOTIFICATION_TEMPLATE = "jde/internationalDropShipTemplateNotification.ftl";
    private static final String TO_EMAIL = "intlds@warn.com";

    @Autowired
    private NotificationService notificationService;

    public void sendInternationalDropShipNotification(List<DropShipInternational> dropShipInternationalList, String subject) {

        try {

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("dropShipInternationalList", dropShipInternationalList);

            notificationService.sendEmail(TO_EMAIL,
                    subject,
                    notificationService.generateNotificationBody(NOTIFICATION_TEMPLATE, objectMap));

        } catch (Exception e) {
            log.error("", e);
        }

    }

}


