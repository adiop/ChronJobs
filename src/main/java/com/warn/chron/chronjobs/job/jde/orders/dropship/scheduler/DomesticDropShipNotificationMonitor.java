package com.warn.chron.chronjobs.job.jde.orders.dropship.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.job.jde.orders.dropship.business.DropShipNotificationService;
import com.warn.chron.chronjobs.job.jde.orders.dropship.entities.domestic.DropShipDomestic;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DomesticDropShipNotificationMonitor {

    private static final String DB_NAME = "\\{DB_JDE_BUSINESS\\}";
    private static final String ADDRRESS_NUMBER = "\\{ADDRRESS_NUMBER\\}";
    private static final String TODAY_DATE = "\\{TODAY_DATE\\}";

    private static final String SHIPMENT_NUMBER = "\\{SHIPMENT_NUMBER\\}";

    private static final String QUERY =  "SELECT DISTINCT SDDOCO AS 'OrderNumber'"
                     + " , SDDCTO AS 'OrderType'"
                     + " , SDAN8 AS 'BillTo'"
                     + " , SDSHAN AS 'ShipTo'"
                     + " , SDVR01 AS 'PONumber'"
                     + " , dbo.JDEDateCodeToDate(SDIVD) AS 'InvoiceDate'"
                     + " , SDDOC AS 'InvoiceNumber'"
                     + " , SDSHPN AS 'ShipmentNumber'"
                     + " FROM {DB_JDE_BUSINESS}.F42119"
                     + " WHERE %s ";

    private static final String TRACKING_SHIPMENT_NUMBER_QUERY = "SELECT [XIREFN]"
            + " FROM {DB_JDE_BUSINESS}.F4217"
            + " WHERE XIREFQ = 'PR' AND XISHPN = '{SHIPMENT_NUMBER}'";

    private static final String PARTNER_INFORMATION_QUERY = "SELECT TOP 1 [WWMLNM] FROM {DB_JDE_BUSINESS}.F0101 "
            + " INNER JOIN {DB_JDE_BUSINESS}.F0111 ON F0101.ABAN8 = F0111.WWAN8"
            + " LEFT JOIN {DB_JDE_BUSINESS}.F0116 ON F0116.ALAN8 = F0101.ABAN8"
            + " LEFT JOIN {DB_JDE_BUSINESS}.F0115 ON F0101.ABAN8 = F0115.WPAN8"
            + " WHERE WWAN8 = '{ADDRRESS_NUMBER}'";

    private static final String ORDERS_BY_ADDRESS_NUMBER_BILLTO_QUERY = String.format(QUERY,
            "SDAN8 = {ADDRRESS_NUMBER} AND SDIVD = {TODAY_DATE}");

    private static final String ORDERS_BY_ADDRESS_NUMBER_SHIPTO_QUERY = String.format(QUERY,
            "SDSHAN = {ADDRRESS_NUMBER} AND SDIVD = {TODAY_DATE}");;

    @NonNull
    private ApplicationEnvironmentConfig dbApplicationConfig;

    @NonNull
    private JdbcTemplate jdeBusinessJdbcTemplate;

    @NonNull
    private DropShipNotificationService dropShipNotificationService;

    @Scheduled(fixedRate = 1000000)
//    @Scheduled(cron = "0 0 21 * * ?")
    public void execute() {

        processOrders("12020",false,"Order Ship Notification - Cabelas"); //Cabelas

        processOrders("86974",false,"Order Ship Notification - Order Invoices"); //Uhaul

        processOrders("113307",true,"Order Ship Notification - Send Paper Invoice"); //JohnDeere
    }

    public void processOrders(String accountNumber, boolean useShipTo, String emailSubject) {

        System.out.println("#########################");
        System.out.println(getQueryToProcessOrders(accountNumber, useShipTo));
        System.out.println("#########################");

        List<DropShipDomestic> dropShipDomesticList = jdeBusinessJdbcTemplate.query(
                getQueryToProcessOrders(accountNumber,useShipTo),
                (rs, rowNum) ->
                        new DropShipDomestic(
                                rs.getString("OrderNumber"),
                                rs.getString("OrderType"),
                                rs.getString("BillTo"),
                                rs.getString("ShipTo"),
                                rs.getString("PONumber"),
                                rs.getString("InvoiceDate"),
                                rs.getString("InvoiceNumber"),
                                rs.getString("ShipmentNumber")
                        )
        );

        if (!dropShipDomesticList.isEmpty())
            dropShipDomesticList.parallelStream().forEach(dropShipDomestic -> {
                dropShipDomestic.setTrackingNumber(
                        getTrackingShipmentNumber(dropShipDomestic.getShipmentNumber()));

                dropShipDomestic.setAccountName(
                        getPartnerNameInformation(accountNumber));
            });

        if (!dropShipDomesticList.isEmpty()) {
            dropShipDomesticList.forEach(System.out::println);
            dropShipNotificationService.sendDomesticDropShipNotification(dropShipDomesticList,emailSubject);
        }
    }

    public String getTrackingShipmentNumber(String shipmentNumber) {
        System.out.println(getQueryTrackingShipmentNumber(shipmentNumber));

        try {
            return jdeBusinessJdbcTemplate.queryForObject(
                    getQueryTrackingShipmentNumber(shipmentNumber),
                    new Object[] { },
                    String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    public String getPartnerNameInformation (String accountNumber) {
        try {
            return jdeBusinessJdbcTemplate.queryForObject(
                    getQueryPartnerInformation(accountNumber),
                    new Object[] { },
                    String.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String getQueryToProcessOrders(String accountNumber, boolean useShipTo) {
        return ((useShipTo) ? ORDERS_BY_ADDRESS_NUMBER_SHIPTO_QUERY : ORDERS_BY_ADDRESS_NUMBER_BILLTO_QUERY)
                .replaceAll(DB_NAME, dbApplicationConfig.getJdeBusiness().getDb())
                .replaceAll(ADDRRESS_NUMBER,accountNumber)
                .replaceAll(TODAY_DATE,"dbo.CdateToJDEdate(dateadd(day, -56, convert(date, GETDATE())))");
    }

    public String getQueryTrackingShipmentNumber(String shipmentNumber) {
        return TRACKING_SHIPMENT_NUMBER_QUERY
                .replaceAll(DB_NAME, dbApplicationConfig.getJdeBusiness().getDb())
                .replaceAll(SHIPMENT_NUMBER,shipmentNumber);
    }

    public String getQueryPartnerInformation(String accountNumber) {
        return PARTNER_INFORMATION_QUERY
                .replaceAll(DB_NAME, dbApplicationConfig.getJdeBusiness().getDb())
                .replaceAll(ADDRRESS_NUMBER,accountNumber);
    }
}
