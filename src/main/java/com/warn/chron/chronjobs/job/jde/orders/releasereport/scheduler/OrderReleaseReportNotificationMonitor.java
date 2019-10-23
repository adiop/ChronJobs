package com.warn.chron.chronjobs.job.jde.orders.releasereport.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.job.jde.common.business.PartnerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReleaseReportNotificationMonitor {

    private static final String DB_JDE_BUSINESS = "\\{DB_JDE_BUSINESS\\}";
    private static final String DB_PARTNERS = "\\{DB_PARTNERS\\}";


    private static final String PARTNERS_TO_ACNOWLEDGE = "SELECT distinct partner_number"
            + " FROM {DB_PARTNERS}.dbo.logins"
            + " inner join {DB_PARTNERS}.dbo.partner_roles pr on logins.user_name = pr.user_name and pr.role_name='supplier'"
            + " left join {DB_PARTNERS}.dbo.acknowledge_mode_exclusion ame on logins.partner_number = ame.partner_number_to_exclude"
            + " where ame.partner_number_to_exclude is null";

    private static final String PARTNER_EMAIL = "select EAEMAL from {DB_JDE_BUSINESS}.F01151"
            + " where (EAETP='ORCC') and EAAN8= %s";

    @NonNull
    private ApplicationEnvironmentConfig dbApplicationConfig;

    @NonNull
    private JdbcTemplate jdeBusinessJdbcTemplate;

    @NonNull
    private PartnerService partnerService;

//    @Scheduled(cron = "0 30 18 * * ?")
    public void execute () {

        System.out.println("##" + partnerService.getPartnerInformation("12020"));

        System.out.println("#####################");

        partnerService.getAllPartnerInformation().values().forEach(System.out::println);
    }

}
