package com.warn.chron.chronjobs.job.pdm.ecns.entities;

import lombok.Data;

public @Data class EcnPart {
    private String partNumber;
    private String revNumber;
    private String ecnStatusText;
    private String ecnStatusColor;
    private Integer daysAssigned;
}
