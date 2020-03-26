package com.warn.chron.chronjobs.job.supplier.performance.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.job.supplier.performance.entities.Supplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateSupplierPerformanceTable {

    private static final String TRUNCATE_QUERY = "truncate table %DB_INTRANET%.dbo.supplier_performance";

    private static final String INSERT_QUERY = "INSERT INTO %DB_INTRANET%.[dbo].[supplier_performance] VALUES(?,?,?,?,?,?,?,?,?) ";
    
    private static final String RETRIEVE_SUPPLIERS_QUERY = "SELECT DISTINCT(f0101.aban8) AS address_book_number, " +
            "                Rtrim(Ltrim(wwmlnm)) AS supplier_name , " +
            "                Rtrim(Ltrim(abac28)) AS commodity_code , " +
            "                0.00 AS pd_last_mo, " +
            "                0.00 AS pd_last_12_mo, " +
            "                0.00 AS pd_cal_yr, " +
            "                CASE " +
            "                                WHEN Len(Rtrim(Ltrim(abac29)))>0 THEN Rtrim(Ltrim(abac29)) " +
            "                                ELSE 'U' " +
            "                END                  AS supplier_status , " +
            "                rtrim(ltrim(abac30)) AS quality_status , " +
            "                CASE rtrim(ltrim(abac27)) " +
            "                                WHEN '' THEN 'LG' " +
            "                                ELSE rtrim(ltrim(abac27)) " +
            "                END AS business_type " +
            "FROM            %DB_JDE_BUSINESS%.[F0101] " +
            "INNER JOIN      %DB_JDE_BUSINESS%.f0111 " +
            "ON              f0101.aban8 = f0111.wwan8 " +
            "AND             f0111.wwidln=0 " +
            "LEFT JOIN       %DB_JDE_BUSINESS%.f0116 " +
            "ON              f0116.alan8 = f0101.aban8 " +
            "LEFT JOIN       %DB_JDE_BUSINESS%.f0115 " +
            "ON              f0101.aban8 = f0115.wpan8 " +
            "WHERE           ( " +
            "                                abat1 = 'v' " +
            "                OR              abat1 = 'vp')";

    @NonNull
    private ApplicationEnvironmentConfig dbApplicationConfig;

    @NonNull
    private JdbcTemplate intranetJdbcTemplate;

    @NonNull
    private JdbcTemplate jdeBusinessJdbcTemplate;

    @Scheduled(cron = "0 5 4 * * ?")
    public void execute() {
        String truncate_query = TRUNCATE_QUERY
                .replaceAll("%DB_INTRANET%", dbApplicationConfig.getIntranet().getDb());


        String retrieve_suppliers_query = RETRIEVE_SUPPLIERS_QUERY
                .replaceAll("%DB_JDE_BUSINESS%", dbApplicationConfig.getJdeBusiness().getDb());


        intranetJdbcTemplate.execute(truncate_query);

        List<Supplier> supplierList = jdeBusinessJdbcTemplate.query(
                    retrieve_suppliers_query,
                    (rs, rowNum) ->
                            new Supplier(
                                    rs.getInt("address_book_number"),
                                    rs.getString("supplier_name"),
                                    rs.getString("commodity_code"),
                                    rs.getDouble("pd_last_mo"),
                                    rs.getDouble("pd_last_12_mo"),
                                    rs.getDouble("pd_cal_yr"),
                                    rs.getString("supplier_status"),
                                    rs.getString("quality_status"),
                                    rs.getString("business_type")
                            )
        );


        System.out.println(supplierList.size());

        if (supplierList.size() > 0)
            batchInsertSuppliers(supplierList);


    }

    public int[] batchInsertSuppliers (List<Supplier> suppliers) {

        String insert_query = INSERT_QUERY
                .replaceAll("%DB_INTRANET%", dbApplicationConfig.getIntranet().getDb());

        return intranetJdbcTemplate.batchUpdate(
                insert_query,
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, suppliers.get(i).getAddressBook());
                        ps.setString(2, suppliers.get(i).getSupplierName());
                        ps.setString(3,suppliers.get(i).getCommodityCode());
                        ps.setDouble(4,suppliers.get(i).getPdLastMo());
                        ps.setDouble(5,suppliers.get(i).getPdLast12Mo());
                        ps.setDouble(6,suppliers.get(i).getPdCalyr());
                        ps.setString(7,suppliers.get(i).getSupplierStatus());
                        ps.setString(8,suppliers.get(i).getQualityStatus());
                        ps.setString(9,suppliers.get(i).getBusinessType());
                    }

                    public int getBatchSize() {
                        return suppliers.size();
                    }

                });
    }

}
