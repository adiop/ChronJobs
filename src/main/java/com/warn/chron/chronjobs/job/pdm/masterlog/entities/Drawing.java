package com.warn.chron.chronjobs.job.pdm.masterlog.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@AllArgsConstructor
public @Data class Drawing {
    private String partNumber;
    private String partRevision;
    private String drawingVersion;
    private String pathToDrawing;
    private String uncServer;
    private String fileName;
    private Date fileDateTime;
    private Integer fileIndex;
    private byte[] fileData;
}
