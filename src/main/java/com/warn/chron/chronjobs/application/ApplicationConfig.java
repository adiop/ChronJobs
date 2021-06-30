package com.warn.chron.chronjobs.application;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.application.common.RestServiceConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean(name = "intranetDataSource")
    @Qualifier("intranetDataSource")
    public DataSource intranetDataSource() {return environmentConfig.getIntranet().getDataSource(); }

//    @Bean(name="intranet")
//    public JdbcTemplate intranetJdbcTemplate(DataSource intranetDataSource) { return new JdbcTemplate(intranetDataSource); }

    @Bean(name="intranetJdbcTemplate")
    public JdbcTemplate intranetJdbcTemplate(@Qualifier("intranetDataSource") DataSource intranetDataSource) { return new JdbcTemplate(intranetDataSource); }

    @Bean(name = "pdmDataSource")
    @Qualifier("pdmDataSource")
    public DataSource pdmDataSource() {return environmentConfig.getPdm().getDataSource(); }

//    @Bean(name="pdm")
//    public JdbcTemplate pdmJdbcTemplate(DataSource pdmDataSource) { return new JdbcTemplate(pdmDataSource); }

    @Bean(name="pdmJdbcTemplate")
    public JdbcTemplate pdmJdbcTemplate(@Qualifier("pdmDataSource") DataSource pdmDataSource) {
        return new JdbcTemplate(pdmDataSource);
    }

    @Bean(name = "crmDataSource")
    @Qualifier("crmDataSource")
    public DataSource crmDataSource() {
        return environmentConfig.getCrm().getDataSource();
    }

//    @Bean(name="crm")
//    public JdbcTemplate crmJdbcTemplate(DataSource crmDataSource) {
//        return new JdbcTemplate(crmDataSource);
//    }

    @Bean(name="crmJdbcTemplate")
    public JdbcTemplate crmJdbcTemplate(@Qualifier("crmDataSource") DataSource crmDataSource) {
        return new JdbcTemplate(crmDataSource);
    }

    @Bean(name = "partnersDataSource")
    @Qualifier("partnersDataSource")
    public DataSource partnersDataSource() {
        return environmentConfig.getPartners().getDataSource();
    }

//    @Bean(name = "partners")
//    public JdbcTemplate partnersJdbcTemplate(DataSource partnersDataSource) { return new JdbcTemplate(partnersDataSource); }

    @Bean(name = "partnersJdbcTemplate")
    public JdbcTemplate partnersJdbcTemplate(@Qualifier("partnersDataSource") DataSource partnersDataSource) {
        return new JdbcTemplate(partnersDataSource);
    }

    @Bean(name = "jdeBusinessDataSource")
    @Qualifier("jdeBusinessDataSource")
    public DataSource jdeBusinessDataSource() {
        return environmentConfig.getJdeBusiness().getDataSource();
    }

//    @Bean(name="jdeBusiness")
//    public JdbcTemplate jdeBusinessJdbcTemplate(DataSource jdeBusinessDataSource) {
//        return new JdbcTemplate(jdeBusinessDataSource);
//    }

    @Bean(name = "jdeTableDataSource")
    @Qualifier("jdeTableDataSource")
    public DataSource jdeTableDataSource() {return environmentConfig.getJdeTable().getDataSource();}

    @Bean(name ="jdeTableJdbcTemplate")
    public JdbcTemplate jdeTableJdbcTemplate (@Qualifier("jdeTableDataSource") DataSource jdeTableDataSource) {
        return new JdbcTemplate(jdeTableDataSource);
    }

    @Bean(name="jdeBusinessJdbcTemplate")
    public JdbcTemplate jdeBusinessJdbcTemplate(@Qualifier("jdeBusinessDataSource") DataSource jdeBusinessDataSource) {
        return new JdbcTemplate(jdeBusinessDataSource);
    }

    @Bean(name = "jdeControlDataSource")
    @Qualifier("jdeControlDataSource")
    public DataSource jdeControlDataSource() {
        return environmentConfig.getJdeControl().getDataSource();
    }

//    @Bean(name="jdeControl")
//    public JdbcTemplate jdeControlJdbcTemplate(DataSource jdeControlDataSource) {
//        return new JdbcTemplate(jdeControlDataSource);
//    }

    @Bean(name="jdeControlJdbcTemplate")
    public JdbcTemplate jdeControlJdbcTemplate(@Qualifier("jdeControlDataSource") DataSource jdeControlDataSource) {
        return new JdbcTemplate(jdeControlDataSource);
    }

    @Bean(name = "jdeSystemDataSource")
    @Qualifier("jdeSystemDataSource")
    public DataSource jdeSystemDataSource() {
        return environmentConfig.getJdeSystem().getDataSource();
    }

//    @Bean(name="jdeSystem")
//    public JdbcTemplate jdeSystemJdbcTemplate(DataSource jdeSystemDataSource) {
//        return new JdbcTemplate(jdeSystemDataSource);
//    }

    @Bean(name="jdeSystemJdbcTemplate")
    public JdbcTemplate jdeSystemJdbcTemplate(@Qualifier("jdeSystemDataSource") DataSource jdeSystemDataSource) {
        return new JdbcTemplate(jdeSystemDataSource);
    }

    @Bean(name = "warnApplicationDataSource")
    @Qualifier("warnApplicationDataSource")
    public DataSource warnApplicationDataSource () {return environmentConfig.getWarnApplication().getDataSource();}

//    @Bean(name="warnApplicationJdbcTemplate")
//    public JdbcTemplate WarnApplicationJdbcTemplate(DataSource warnApplicationDataSource) {
//        return new JdbcTemplate((warnApplicationDataSource));
//    }

    @Bean(name="warnApplicationJdbcTemplate")
    public JdbcTemplate WarnApplicationJdbcTemplate(@Qualifier("warnApplicationDataSource") DataSource warnApplicationDataSource) {
        return new JdbcTemplate((warnApplicationDataSource));
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
