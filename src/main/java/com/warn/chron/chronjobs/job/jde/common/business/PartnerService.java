package com.warn.chron.chronjobs.job.jde.common.business;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.job.jde.common.entities.PartnerInformation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.security.interfaces.RSAKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class PartnerService {

    private static final String DB_NAME = "\\{DB_JDE_BUSINESS\\}";

    private static final String PARTNER_INFORMATION_LIST_QUERY = "SELECT DISTINCT ABAN8, "
            + " ABALPH,"
            + " WWMLNM,"
            + " WWGNNM,"
            + " WWSRNM,"
            + " ALADD1,"
            + " ALADD2,"
            + " ALADD3,"
            + " ALADD4,"
            + " ALCTY1,"
            + " ALADDS,"
            + " ALADDZ,"
            + " ALCTR "
            + " FROM {DB_JDE_BUSINESS}.F0101 "
            + " INNER JOIN {DB_JDE_BUSINESS}.F0111 ON F0101.ABAN8 = F0111.WWAN8"
            + " LEFT JOIN {DB_JDE_BUSINESS}.F0116 ON F0116.ALAN8 = F0101.ABAN8"
            + " LEFT JOIN {DB_JDE_BUSINESS}.F0115 ON F0101.ABAN8 = F0115.WPAN8";

    // Create a cache for PartnerInformation based on their account number
    LoadingCache<String, Map<String, PartnerInformation>> allPartnersInformationCache =
            CacheBuilder.newBuilder()
                    .expireAfterAccess(1, TimeUnit.HOURS)
                    .build(
                            new CacheLoader<String, Map<String, PartnerInformation>>() {
                                @Override
                                public Map<String, PartnerInformation> load(String key) throws Exception {

                                    Map<String, PartnerInformation> partnerInformationMap = new HashMap<>();

                                    try {
                                        List<PartnerInformation> partnerInformationList = jdeBusinessJdbcTemplate.query(
                                                getQueryPartnerInformationList(),
                                                (rs, rowNum) ->
                                                        new PartnerInformation(
                                                                String.valueOf(rs.getInt("ABAN8")),
                                                                (rs.getString("ABALPH")).trim(),
                                                                (rs.getString("WWMLNM")).trim(),
                                                                (rs.getString("WWGNNM") + " " + rs.getString("WWSRNM")).trim(),
                                                                (rs.getString("ALADD1") + "").trim(),
                                                                (rs.getString("ALADD2") + "").trim(),
                                                                (rs.getString("ALADD3") + "").trim(),
                                                                (rs.getString("ALADD4") + "").trim(),
                                                                (rs.getString("ALCTY1") + "").trim(),
                                                                (rs.getString("ALADDS") + "").trim(),
                                                                (rs.getString("ALADDZ") + "").trim(),
                                                                (rs.getString("ALCTR") + "").trim()
                                                        ));

                                        partnerInformationList.stream().forEach(p -> partnerInformationMap.put(p.getAccountNumber(), p));

//                                        partnerInformationMap.keySet().forEach(System.out::println);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    return partnerInformationMap;

                                }
                            });

    @NonNull
    private ApplicationEnvironmentConfig dbApplicationConfig;

    @NonNull
    private JdbcTemplate jdeBusinessJdbcTemplate;

    public Map<String, PartnerInformation> getAllPartnerInformation() {

        try {
            return allPartnersInformationCache.get("all");
        } catch (ExecutionException e) {
            throw new RuntimeException("An error has occurred", e);
        }

    }

    public PartnerInformation getPartnerInformation(String accountNumber) {

        try {
            return allPartnersInformationCache.get("all").get(accountNumber);
        } catch (ExecutionException e) {
            throw new RuntimeException("An error has occurred", e);
        }

    }

    public String getQueryPartnerInformationList () {
        return PARTNER_INFORMATION_LIST_QUERY
                .replaceAll(DB_NAME, dbApplicationConfig.getJdeBusiness().getDb());
    }

}
