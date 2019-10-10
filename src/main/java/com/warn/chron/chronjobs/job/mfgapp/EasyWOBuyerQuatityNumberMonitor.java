package com.warn.chron.chronjobs.job.mfgapp;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EasyWOBuyerQuatityNumberMonitor {

    private static final Integer BUYER_NUMBER = 351950000;

    private static final String QUERY = "SELECT KMKID1 as 'Card #',KMDOCO as 'WO #', KMTRQT/10000  as 'Transaction Quantity', KMKBQT as 'Kan Ban Quantity',  WAANSA as 'Buyer #', KMLITM as 'Part #' " +
            "FROM %DB%.F3016 " +
            "INNER JOIN %DB%.F30161 ON %DB%.F3016.KMKID1 = %DB%.F30161.KDKID1 " +
            "INNER JOIN %DB%.F4801 ON %DB%.F3016.KMDOCO = %DB%.F4801.WADOCO " +
            " WHERE %DB%.F3016.KMKBST = 2 AND %DB%.F3016.KMTRQT = ?";

    @NonNull
    private ApplicationEnvironmentConfig dbApplicationConfig;

    @NonNull
    private JdbcTemplate jdeBusinessJdbcTemplate;

//    @Scheduled(fixedRate = 300000)
    public void execute() {
        String query = QUERY.
                replaceAll("%DB%", dbApplicationConfig.getJdeBusiness().getDb());

        jdeBusinessJdbcTemplate.query(query, new Object[]{BUYER_NUMBER}, row -> {

            System.out.println(row.getString("Card #"));

        });

    }


}
