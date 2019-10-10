package com.warn.chron.chronjobs.job.pdm.ecns.entities;

import lombok.Data;

import java.util.List;

public @Data class EcnSummary {
    private String approver;
    private String email;
    private List<Ecn> ecns;
}
