package com.warn.chron.chronjobs.job.jde.utility.upc.entitites;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor public @Data class UPCNumber {
    private String upcNumber;
    private String partNumber;
    private int assigned;
}
