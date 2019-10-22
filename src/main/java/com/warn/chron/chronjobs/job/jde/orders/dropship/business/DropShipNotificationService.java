package com.warn.chron.chronjobs.job.jde.orders.dropship.business;

import com.warn.chron.chronjobs.business.notification.NotificationService;
import com.warn.chron.chronjobs.job.jde.orders.dropship.entities.domestic.DropShipDomestic;
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
    private static final String INTERNATIONAL_NOTIFICATION_TEMPLATE = "jde/internationalDropShipTemplateNotification.ftl";
    private static final String INTERNATIONAL_TO_EMAIL = "intlds@warn.com";

    private static final String DOMESTIC_NOTIFICATION_TEMPLATE = "jde/domesticDropShipTemplateNotification.ftl";
    private static final String DOMESTIC_TO_EMAIL = "accountsreceivable@warn.com";

    @Autowired
    private NotificationService notificationService;

    public void sendDomesticDropShipNotification (List<DropShipDomestic> dropShipDomesticList, String subject) {
        System.out.println("#######################");
        System.out.println(dropShipDomesticList.toString());
        System.out.println("#######################");

        try {

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("dropShipDomesticList", dropShipDomesticList);

            notificationService.sendEmail(DOMESTIC_TO_EMAIL,
                    subject ,
                    notificationService.generateNotificationBody(DOMESTIC_NOTIFICATION_TEMPLATE, objectMap));

        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void sendInternationalDropShipNotification(List<DropShipInternational> dropShipInternationalList, String subject) {
        System.out.println("#######################");
        System.out.println(dropShipInternationalList.toString());
        System.out.println("#######################");
        try {

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("dropShipInternationalList", dropShipInternationalList);

            notificationService.sendEmail( INTERNATIONAL_TO_EMAIL,
                    subject,
                    notificationService.generateNotificationBody(INTERNATIONAL_NOTIFICATION_TEMPLATE, objectMap));

        } catch (Exception e) {
            log.error("", e);
        }

    }

}


