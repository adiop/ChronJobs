package com.warn.chron.chronjobs.application.common;

import com.warn.chron.chronjobs.application.common.DatabaseInformation;
import com.warn.chron.chronjobs.application.common.RestServiceConfig;

public interface ApplicationEnvironmentConfig {
    public DatabaseInformation getIntranet();

    public DatabaseInformation getPdm();

    public DatabaseInformation getCrm();

    public DatabaseInformation getJdeBusiness();

    public DatabaseInformation getJdeControl();

    public DatabaseInformation getJdeSystem();

    public RestServiceConfig getNotificationRestService();
}
