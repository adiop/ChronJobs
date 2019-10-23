package com.warn.chron.chronjobs.application;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.application.common.DatabaseInformation;
import com.warn.chron.chronjobs.application.common.RestServiceConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
@ConfigurationProperties("prod")
public @Data class ApplicationProdConfig implements ApplicationEnvironmentConfig {
    private DatabaseInformation intranet;

    private DatabaseInformation pdm;

    private DatabaseInformation crm;

    private DatabaseInformation partners;

    private DatabaseInformation jdeBusiness;

    private DatabaseInformation jdeControl;

    private DatabaseInformation jdeSystem;

    private RestServiceConfig notificationRestService;

}
