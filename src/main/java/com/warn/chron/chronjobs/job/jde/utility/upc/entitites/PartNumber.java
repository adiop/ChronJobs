package com.warn.chron.chronjobs.job.jde.utility.upc.entitites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public @Data class PartNumber {
    @NonNull
    private String partNumber;

    @NonNull
    private String description;

    private String upcNmber;
}
