package com.warn.chron.chronjobs.job.jde.orders.dropship.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.job.jde.orders.dropship.business.DropShipNotificationService;
import com.warn.chron.chronjobs.job.jde.orders.dropship.entities.international.DropShipInternational;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.GregorianCalendar;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InternationalDropShipNotificationMonitor {

    private static final String DB_NAME = "\\{DB_JDE_BUSINESS\\}";

    private static final String ORDERS_TO_BE_SHIPPED_BY_FILTER = "SELECT F4211.SDSHAN, ab.ABALPH as Customer,"
            + " F4211.SDDOCO, F4211.SDDCTO, F4211.SDLITM,"
            + " F4211.SDUORG * .0001 AS SDUORG, F4211.SDPDDJ,"
            + " dbo.JDEDateCodeToDate(F4211.SDPDDJ) AS Today, ab2.ABALPH AS Carrier"
            + " FROM {DB_JDE_BUSINESS}.F4211"
            + " LEFT OUTER JOIN {DB_JDE_BUSINESS}.F0101 ab ON F4211.SDSHAN = ab.ABAN8 AND ab.ABAT1 = 'c'"
            + " LEFT OUTER JOIN {DB_JDE_BUSINESS}.F0101 ab2 ON F4211.SDCARS = ab2.ABAN8 AND ab2.ABAT1 = 'TC'"
            + " WHERE %s"
            + " ORDER BY F4211.SDDOCO";

    private static final String ORDERS_TO_BE_SHIPPED_TODAY = String.format(ORDERS_TO_BE_SHIPPED_BY_FILTER,
            "(F4211.SDDCTO = 'SD') AND (F4211.SDNXTR < 568) AND  (F4211.SDTRDJ = dbo.CdateToJDEdate(GETDATE()))");

    private static final String ORDERS_TO_BE_SHIPPED_BY_DATE = String.format(ORDERS_TO_BE_SHIPPED_BY_FILTER,
            "(F4211.SDDCTO = 'SD') AND (F4211.SDNXTR < 568) AND  (F4211.SDPDDJ = ?)");

    @NonNull
    private ApplicationEnvironmentConfig dbApplicationConfig;

    @NonNull
    private JdbcTemplate jdeBusinessJdbcTemplate;

    @NonNull
    private DropShipNotificationService dropShipNotificationService;

    @Scheduled(fixedRate = 1000000)
    public void execute() {
        checkForShipment(getOrdersToBeShippedTodayQuery(),"International Drop Ship Notification");

        checkForShipment(getOrdersToBePickedYesterday(),"Reminder Scheduled Shipment Past Due International Drop Ship Notification");

        checkForShipment(getOrdersToBePickedIn7Days(), "Reminder Scheduled Shipment Pending International Drop Ship Notification");

    }

    public void checkForShipment (String query, String subject) {
        List<DropShipInternational> dropShipInternationalList = jdeBusinessJdbcTemplate.query(
                query,
                (rs, rowNum) ->
                    new DropShipInternational(
                            rs.getString("SDSHAN"),
                            rs.getString("Customer"),
                            rs.getString("SDDOCO"),
                            rs.getString("SDDCTO"),
                            rs.getString("SDLITM"),
                            rs.getString("SDUORG"),
                            rs.getString("SDPDDJ"),
                            rs.getString("Today"),
                            rs.getString("Carrier")
                    )
        );

        if (!dropShipInternationalList.isEmpty())
            dropShipNotificationService.sendInternationalDropShipNotification(dropShipInternationalList, subject);
    }

    public String getOrdersToBeShippedTodayQuery() {
        return ORDERS_TO_BE_SHIPPED_TODAY
                .replaceAll(DB_NAME, dbApplicationConfig.getJdeBusiness().getDb());
    }

    public String getOrdersToBePickedIn7Days() {
        return ORDERS_TO_BE_SHIPPED_BY_DATE
                .replaceAll(DB_NAME, dbApplicationConfig.getJdeBusiness().getDb())
                .replaceAll("\\?","dbo.CdateToJDEdate(dateadd(day, 7, convert(date, GETDATE())))");
    }

    public String getOrdersToBePickedYesterday() {
        return ORDERS_TO_BE_SHIPPED_BY_DATE
                .replaceAll(DB_NAME, dbApplicationConfig.getJdeBusiness().getDb())
                .replaceAll("\\?","dbo.CdateToJDEdate(dateadd(day, -1, convert(date, GETDATE())))");
    }

}
