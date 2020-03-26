package com.warn.chron.chronjobs.job.supplier.performance.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class Supplier {
    private Integer addressBook;
    private String supplierName;
    private String commodityCode;
    private Double pdLastMo;
    private Double pdLast12Mo;
    private Double pdCalyr;
    private String supplierStatus;
    private String qualityStatus;
    private String businessType;
}
