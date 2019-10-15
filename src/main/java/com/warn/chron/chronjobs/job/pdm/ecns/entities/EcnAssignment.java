package com.warn.chron.chronjobs.job.pdm.ecns.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
public @Data class EcnAssignment {
    private String fullName;
    private String approver;
    private Integer resolution;
    private String ecnNumber;
    private String partNumber;
    private String revNumber;
    private String ecnStatusText;
    private String email;
    private String projectComment;
    private Integer daysAssigned;
    private Date assignedTime;
    private String ecnStatusColor;
    private String reasonCode;
    private String changeDescription;
}
