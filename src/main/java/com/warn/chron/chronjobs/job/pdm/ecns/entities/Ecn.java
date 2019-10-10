package com.warn.chron.chronjobs.job.pdm.ecns.entities;

import lombok.Data;

import java.util.List;

public @Data class Ecn {
    private String ecnNumber;
    private String changeDescription;
    private List<EcnPart> ecnParts;
}
