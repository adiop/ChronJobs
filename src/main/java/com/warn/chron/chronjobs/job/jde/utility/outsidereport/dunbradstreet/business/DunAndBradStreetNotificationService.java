package com.warn.chron.chronjobs.job.jde.utility.outsidereport.dunbradstreet.business;

import com.warn.chron.chronjobs.business.notification.NotificationService;
import com.warn.chron.chronjobs.job.jde.utility.outsidereport.dunbradstreet.entities.EnhanceAccountReceivableObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DunAndBradStreetNotificationService {

    private static final String DUN_AND_BRAD_STREET_TEMPLATE = "dunAndBradStreet/dunAndBradStreetTemplateNotification.ftl";
    //private static final String DUN_AND_BRAD_STREET_TO_EMAIL = "dtwest@dnb.com";
    private static final String DUN_AND_BRAD_STREET_TO_EMAIL = "diopa@warn.com";

    @Autowired
    private NotificationService notificationService;

    public void sendReport (List<EnhanceAccountReceivableObject> enhanceAccountReceivableList) {

        try {

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("enhanceAccountReceivableList", enhanceAccountReceivableList);

            notificationService.sendEmail(DUN_AND_BRAD_STREET_TO_EMAIL,
                    "TRW 6258 As of Warn Industries Inc.",
                    notificationService.generateNotificationBody(DUN_AND_BRAD_STREET_TEMPLATE, objectMap));
        } catch (Exception e) {
            log.error("",e);
        }



    }
}
