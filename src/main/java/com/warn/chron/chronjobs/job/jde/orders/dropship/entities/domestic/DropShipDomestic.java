package com.warn.chron.chronjobs.job.jde.orders.dropship.entities.domestic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public @Data class DropShipDomestic {

    @NonNull
    private String orderNumber;

    @NonNull
    private String orderType;

    @NonNull
    private String billTo;

    @NonNull
    private String shipTo;

    @NonNull
    private String poNumber;

    @NonNull
    private String invoiceDate;

    @NonNull
    private String invoiceNumber;

    @NonNull
    private String shipmentNumber;

    private String trackingNumber;

    private String accountName;
}
