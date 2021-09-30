package com.warn.chron.chronjobs.job.jde.utility.outsidereport.dunbradstreet.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.business.notification.NotificationService;
import com.warn.chron.chronjobs.job.jde.utility.outsidereport.dunbradstreet.business.DunAndBradStreetNotificationService;
import com.warn.chron.chronjobs.job.jde.utility.outsidereport.dunbradstreet.entities.EnhanceAccountReceivableObject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DunAndBradstreet {

    private static String DUN_BRAD_STREET_SQL = "SELECT * FROM %DB_JDE_BUSINESS%.F03B920";

    @NonNull
    private DunAndBradStreetNotificationService dunAndBradStreetNotificationService;

    @NonNull
    private ApplicationEnvironmentConfig dbApplicationConfig;

    @NonNull
    @Qualifier("jdeBusinessJdbcTemplate")
    private JdbcTemplate jdeBusinessJdbcTemplate;

//    @Scheduled(cron = "0 0 21 * * ?")
    @Scheduled(cron = "0 0 21 3 * ?")
    public void execute () {
        doReport();
    }

    public void doReport () {
        String dunAndBradStreet_SQL = DUN_BRAD_STREET_SQL
                .replaceAll("%DB_JDE_BUSINESS%", dbApplicationConfig.getJdeBusiness().getDb());


        List<EnhanceAccountReceivableObject> enhanceAccountReceivableObjectList = jdeBusinessJdbcTemplate.query(
                dunAndBradStreet_SQL,
                (rs, rowNum) ->
                    new EnhanceAccountReceivableObject(
                            rs.getString("ZCCRTF"),
                            rs.getString("ZCUKID")
                    )
        );

        dunAndBradStreetNotificationService.sendReport(enhanceAccountReceivableObjectList);

    }


}
