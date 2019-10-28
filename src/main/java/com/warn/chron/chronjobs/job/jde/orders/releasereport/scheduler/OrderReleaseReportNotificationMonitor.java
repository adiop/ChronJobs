package com.warn.chron.chronjobs.job.jde.orders.releasereport.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.job.jde.common.business.PartnerService;
import com.warn.chron.chronjobs.job.jde.common.entities.PartnerInformation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
    private JdbcTemplate partnersJdbcTemplate;

    @NonNull
    private PartnerService partnerService;

//    @Scheduled(cron = "0 30 18 * * ?")
    public void execute () {

        findPartnersToAcknowledge().stream().forEach(System.out::println);

    }


    public List<PartnerInformation> findPartnersToAcknowledge() {
        List<String> partnersAccountNumberList =  partnersJdbcTemplate.queryForList(
                getQueryFindPartnersToAcknowledge(),
                String.class
        );

        Collection<PartnerInformation> partnersToAcknowledgeList = filterByKeys(
                            partnerService.getAllPartnerInformation(),
                            partnersAccountNumberList).values();

        return partnersToAcknowledgeList.parallelStream().collect(Collectors.toList());
    }



    public String getQueryFindPartnersToAcknowledge() {
        return PARTNERS_TO_ACNOWLEDGE
                .replaceAll(DB_PARTNERS, dbApplicationConfig.getPartners().getDb());
    }

    public static <K, V> Map<K, V> filterByKeys(final Map<K, V> map, final Iterable<K> keys) {
        final Map<K, V> newMap = new HashMap<>();
        keys.forEach(key -> Optional.ofNullable(map.get(key))
                .ifPresent(value -> newMap.put(key, value)));
        return newMap;
    }

}