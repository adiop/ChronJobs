package com.warn.chron.chronjobs.job.pdm.ecns.business;

import com.warn.chron.chronjobs.business.notification.NotificationService;
import com.warn.chron.chronjobs.job.pdm.ecns.entities.EcnSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EcnNotificationService {
    @Autowired
    private NotificationService notificationService;

    public void sendEcnSummary (EcnSummary ecnSummary) {

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("ecnSummary", ecnSummary);

        notificationService.sendEmail(ecnSummary.getEmail(),"ECN Assigned Items Summary",
                notificationService.generateNotificationBody("ecns/ecnSummaryTemplateNotification.ftl",objectMap));
    }

    public Boolean sendEmail(String to, String subject, String body) {

        return null;
    }
}
