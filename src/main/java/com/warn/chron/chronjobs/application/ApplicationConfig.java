package com.warn.chron.chronjobs.application;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.application.common.RestServiceConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories
@RequiredArgsConstructor
public class ApplicationConfig {

    @NonNull
    private ApplicationEnvironmentConfig environmentConfig;

    @Bean
    public DataSource intranetDataSource() {return environmentConfig.getIntranet().getDataSource(); }

    @Bean
    public JdbcTemplate intranetJdbcTemplate(DataSource intranetDataSource) { return new JdbcTemplate(intranetDataSource); }

    @Bean
    public DataSource pdmDataSource() {return environmentConfig.getPdm().getDataSource(); }

    @Bean
    public JdbcTemplate pdmJdbcTemplate(DataSource pdmDataSource) { return new JdbcTemplate(pdmDataSource); }

    @Bean
    public DataSource crmDataSource() {
        return environmentConfig.getCrm().getDataSource();
    }

    @Bean
    public JdbcTemplate crmJdbcTemplate(DataSource crmDataSource) {
        return new JdbcTemplate(crmDataSource);
    }

    @Bean
    public DataSource partnersDataSource() {return environmentConfig.getPartners().getDataSource(); }

    @Bean
    public JdbcTemplate partnersJdbcTemplate(DataSource partnersDataSource) { return new JdbcTemplate(partnersDataSource); }

    @Bean
    public DataSource jdeBusinessDataSource() {
        return environmentConfig.getJdeBusiness().getDataSource();
    }

    @Bean
    public JdbcTemplate jdeBusinessJdbcTemplate(DataSource jdeBusinessDataSource) {
        return new JdbcTemplate(jdeBusinessDataSource);
    }

    @Bean
    public DataSource jdeControlDataSource() {
        return environmentConfig.getJdeControl().getDataSource();
    }

    @Bean
    public JdbcTemplate jdeControlJdbcTemplate(DataSource jdeControlDataSource) {
        return new JdbcTemplate(jdeControlDataSource);
    }

    @Bean
    public DataSource jdeSystemDataSource() {
        return environmentConfig.getJdeSystem().getDataSource();
    }

    @Bean
    public JdbcTemplate jdeSystemJdbcTemplate(DataSource jdeSystemDataSource) {
        return new JdbcTemplate(jdeSystemDataSource);
    }


    /**
     * Notification Rest Service Configuration
     */
    @Bean
    public RestServiceConfig notificationRestServiceConfig() {
        return environmentConfig.getNotificationRestService();
    }

    @Bean
    public RestServiceConfig notificationAuthenticatedRestService() { return environmentConfig.getNotificationAuthenticatedRestService(); }

}
