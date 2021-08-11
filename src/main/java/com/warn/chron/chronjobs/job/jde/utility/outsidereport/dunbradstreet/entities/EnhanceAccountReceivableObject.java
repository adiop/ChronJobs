package com.warn.chron.chronjobs.job.jde.utility.outsidereport.dunbradstreet.entities;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public @Data class EnhanceAccountReceivableObject {

    @NonNull
    private String creditReportingTapeFormat; //ZCCRTF

    @NonNull
    private String uniqueKeyID; //ZCUKID


}
