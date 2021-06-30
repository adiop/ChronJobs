package com.warn.chron.chronjobs.application.common;

import com.warn.chron.chronjobs.application.common.DatabaseInformation;
import com.warn.chron.chronjobs.application.common.RestServiceConfig;

import javax.xml.crypto.Data;

public interface ApplicationEnvironmentConfig {
    public DatabaseInformation getIntranet();

    public DatabaseInformation getPdm();

    public DatabaseInformation getCrm();

    public DatabaseInformation getPartners();

    public DatabaseInformation getWarnApplication();

    public DatabaseInformation getJdeBusiness();

    public DatabaseInformation getJdeControl();

    public DatabaseInformation getJdeSystem();

    public DatabaseInformation getJdeTable();

    public RestServiceConfig getNotificationRestService();

    public RestServiceConfig getNotificationAuthenticatedRestService();
}
