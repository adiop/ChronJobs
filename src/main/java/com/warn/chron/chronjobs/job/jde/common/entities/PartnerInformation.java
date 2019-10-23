package com.warn.chron.chronjobs.job.jde.common.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class PartnerInformation {
    private String accountNumber;
    private String accountName;
    private String mailName;
    private String contact;

    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;

    private String city;
    private String state;
    private String zip;
    private String country;
}
