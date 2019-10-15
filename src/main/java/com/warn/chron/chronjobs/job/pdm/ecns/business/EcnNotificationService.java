package com.warn.chron.chronjobs.job.pdm.ecns.business;

import com.warn.chron.chronjobs.business.notification.NotificationService;
import com.warn.chron.chronjobs.job.pdm.ecns.entities.EcnSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EcnNotificationService {

    private static final String NOTIFICATION_SUBJECT = "ECN Assigned Items Summary";

    private static final String NOTIFICATION_TEMPLATE = "ecns/ecnSummaryTemplateNotification.ftl";

    @Autowired
    private NotificationService notificationService;

    public void sendEcnSummary(EcnSummary ecnSummary) {

        try {

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("ecnSummary", ecnSummary);

            notificationService.sendEmail(ecnSummary.getEmail(),
                    NOTIFICATION_SUBJECT,
                    notificationService.generateNotificationBody(NOTIFICATION_TEMPLATE, objectMap));

        } catch (Exception e) {
            log.error("", e);
        }

    }

}
