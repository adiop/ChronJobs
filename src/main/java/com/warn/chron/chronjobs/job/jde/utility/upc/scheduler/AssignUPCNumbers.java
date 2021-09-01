package com.warn.chron.chronjobs.job.jde.utility.upc.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.job.jde.utility.upc.entitites.PartNumber;
import com.warn.chron.chronjobs.job.jde.utility.upc.entitites.UPCNumber;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AssignUPCNumbers {

    private static final String GET_PART_NUMBER_WITH_NO_UPC_QUERY = "SELECT IMLITM, IMDSC1 FROM %DB_JDE_BUSINESS%.F4101 WHERE IMUPCN = ''";

    private static final  String CHECK_IF_UPC_IS_AVAILABLE_QUERY = "SELECT * FROM %DB_WARN_APPLICATION%.dbo.UPCNumbers WHERE CoreUPCNumber = ?";

    private static final String UPDATE_UPC_WITH_PART_NUMBER_QUERY = "UPDATE %DB_WARN_APPLICATION%.dbo.UPCNumbers SET LITM = ?, assigned = ? WHERE CoreUPCNumber = ?";

    private static final String UPDATE_ITEM_MASTER_WITH_NEWLY_CREATED_UPC_CODES = "USE %DB_JDE_BUSINESS_TABLE%;\n" +
            "\n" +
            "\n" +
            "SET ANSI_NULLS ON;\n" +
            "\n" +
            "\n" +
            "SET QUOTED_IDENTIFIER ON;\n" +
            "\n" +
            "\n" +
            "SET ANSI_PADDING ON;\n" +
            "\n" +
            "\n" +
            "\n" +
            "CREATE TABLE [%DB_JDE_BUSINESS_TABLE%].[dbo].[F4104_wk](\n" +
            "       [IVAN8] [float] NOT NULL,\n" +
            "       [IVXRT] [char](2) NOT NULL,\n" +
            "       [IVITM] [float] NOT NULL,\n" +
            "       [IVEXDJ] [numeric](18, 0) NOT NULL,\n" +
            "       [IVEFTJ] [numeric](18, 0) NULL,\n" +
            "       [IVMCU] [char](12) NULL,\n" +
            "       [IVCITM] [char](25) NOT NULL,\n" +
            "       [IVDSC1] [char](30) NULL,\n" +
            "       [IVDSC2] [char](30) NULL,\n" +
            "       [IVALN] [char](30) NULL,\n" +
            "       [IVLITM] [char](25) NULL,\n" +
            "       [IVAITM] [char](25) NULL,\n" +
            "       [IVURCD] [char](2) NULL,\n" +
            "       [IVURDT] [numeric](18, 0) NULL,\n" +
            "       [IVURAT] [float] NULL,\n" +
            "       [IVURAB] [float] NULL,\n" +
            "       [IVURRF] [char](15) NULL,\n" +
            "       [IVUSER] [char](10) NULL,\n" +
            "       [IVPID] [char](10) NULL,\n" +
            "       [IVJOBN] [char](10) NULL,\n" +
            "       [IVUPMJ] [numeric](18, 0) NULL,\n" +
            "       [IVTDAY] [float] NULL,\n" +
            "       [IVRATO] [float] NULL,\n" +
            "       [IVAPSP] [float] NULL,\n" +
            "       [IVIDEM] [char](1) NULL,\n" +
            "       [IVAPSS] [char](1) NULL,\n" +
            "       [IVCIRV] [char](20) NOT NULL,\n" +
            "       [IVADIND] [char](1) NULL,\n" +
            "       [IVBPIND] [char](1) NULL,\n" +
            "       [IVCARDNO] [char](4) NULL\n" +
            ") ON [PRIMARY];\n" +
            "\n" +
            "\n" +
            "\n" +
            "SET ANSI_PADDING OFF;\n" +
            "\n" +
            "\n" +
            "\n" +
            "insert into [%DB_JDE_BUSINESS_TABLE%].[dbo].F4104_wk\n" +
            "    Select '100','UP',IMITM,199365,115067,null,\n" +
            "       'UPC_Number',IMDSC1,IMDSC2,null,IMLITM,\n" +
            "       IMAITM,null,null,null,null,null,null,'VISUALCRON','UPCLOAD','115068','110000',null,null,null,null,' ',null,null,null \n" +
            "    From %DB_JDE_BUSINESS%.F4101  \n" +
            "        where not exists  \n" +
            "         (select * from %DB_JDE_BUSINESS%.F4104 where ivitm = imitm);\n" +
            "\n" +
            "update [%DB_JDE_BUSINESS_TABLE%].[dbo].F4104_wk\n" +
            "   set ivcitm = (substring(UPC_Number,2,len(UPC_Number)))\n" +
            "   from [%DB_WARN_APPLICATION%].[DBO].UPCNumbers \n" +
            "        where ivLITM = LITM  and assigned = 1;\n" +
            "\n" +
            "delete from [%DB_JDE_BUSINESS_TABLE%].[dbo].F4104_wk\n" +
            "  where ivcitm = 'UPC_Number';\n" +
            "\n" +
            "insert into %DB_JDE_BUSINESS%.F4104\n" +
            "    Select  max(a.IVAN8), max(a.IVXRT), a.IVITM, max(a.IVEXDJ), \n" +
            "                     max(a.IVEFTJ), max(a.IVMCU), max(a.IVCITM),\n" +
            "                     max(a.IVDSC1), max(a.IVDSC2), max(a.IVALN), \n" +
            "                     max(a.IVLITM), max(a.IVAITM), max(a.IVURCD),\n" +
            "                     max(a.IVURDT), max(a.IVURAT), max(a.IVURAB), \n" +
            "                     max(a.IVURRF), max(a.IVUSER), max(a.IVPID),\n" +
            "                     max(a.IVJOBN), max(a.IVUPMJ), max(a.IVTDAY),\n" +
            "                     max(a.IVRATO), max(a.IVAPSP), max(a.IVIDEM), \n" +
            "                     max(a.IVAPSS), max(a.IVCIRV), max(a.IVADIND),\n" +
            "                     max(a.IVBPIND), max(a.IVCARDNO)\n" +
            "    From [%DB_JDE_BUSINESS_TABLE%].[dbo].F4104_wk as a\n" +
            "       group by ivitm;\n" +
            "\n" +
            "\n" +
            "\n" +
            "update %DB_JDE_BUSINESS%.F4101 set IMUPCN = UPC_Number from [%DB_WARN_APPLICATION%].[DBO].UPCNumbers where ltrim(rtrim([F4101].IMLITM)) = ltrim(rtrim([UPCNumbers].LITM)) and IMUPCN <> UPC_Number;\n"
    ;

//    private static final String DROPF4101_WK = "DROP TABLE [%DB_JDE_BUSINESS_TABLE%].[dbo].[F4104_wk];";
    private static final String DROPF4101_WK = "IF OBJECT_ID('[%DB_JDE_BUSINESS_TABLE%].[dbo].[F4104_wk]', 'U') IS NOT NULL DROP TABLE [%DB_JDE_BUSINESS_TABLE%].[dbo].[F4104_wk];";

    @NonNull
    private ApplicationEnvironmentConfig dbApplicationConfig;

    @NonNull
    @Qualifier("jdeBusinessJdbcTemplate")
    private JdbcTemplate jdeBusinessJdbcTemplate;

    @NonNull
    @Qualifier("warnApplicationJdbcTemplate")
    private JdbcTemplate warnApplicationJdbcTemplate;

    @NonNull
    @Qualifier("jdeTableJdbcTemplate")
    private JdbcTemplate jdeTableJdbcTemplate;


    //    @Scheduled(fixedRate = 1000000)
//    @Scheduled(cron = "0 0 21 * * ?")
    public void execute () {

        // Get part number with no upc and assign them

        List<PartNumber> partNumberListWithNoUPC = getPartNumbersWithNoUPC();

        partNumberListWithNoUPC.stream().forEach(partNumber -> {assignUPCToPartNumber(partNumber.getPartNumber());});

        // update item master with new UPC codes.

        updateItemMasterWithNewUPCCodes ();

        // drop working table
        dropf4101WkTable();

    }

    public List<PartNumber> getPartNumbersWithNoUPC () {
        String getPartNumberWithNoUPC_query = GET_PART_NUMBER_WITH_NO_UPC_QUERY
                .replaceAll("%DB_JDE_BUSINESS%", dbApplicationConfig.getJdeBusiness().getDb());

        List<PartNumber> partNumberList = jdeBusinessJdbcTemplate.query(
            getPartNumberWithNoUPC_query, (rs, rowNum) ->
                        new PartNumber(
                                rs.getString("IMLITM"),
                                rs.getString("IMDSC1")
                        )
        );
        List<PartNumber> filteredListOfPartnumber = partNumberList.stream()
                .filter(partNumber -> partNumber.getPartNumber().trim()!= null)
                .filter(partNumber -> partNumber.getPartNumber().trim().matches("[0-9]+"))
                .collect(Collectors.toList())
        ;
        return filteredListOfPartnumber;
    }

    public void assignUPCToPartNumber(String partNumber) {
        String checkIfUPCIsAvailable_query = CHECK_IF_UPC_IS_AVAILABLE_QUERY
                .replaceAll("%DB_WARN_APPLICATION%", dbApplicationConfig.getWarnApplication().getDb());

        String updateUPCWithPartNumber_query = UPDATE_UPC_WITH_PART_NUMBER_QUERY
                .replaceAll("%DB_WARN_APPLICATION%", dbApplicationConfig.getWarnApplication().getDb());

        List<UPCNumber> upcNumbersListForPartNumber = warnApplicationJdbcTemplate.query(
                checkIfUPCIsAvailable_query,
                new Object[]{partNumber},
                (rs,rowNum) ->
                        new UPCNumber(
                                rs.getString("UPC_Number"),
                                rs.getString("LITM"),
                                rs.getInt("assigned")
                        )

        );

        if (upcNumbersListForPartNumber.size() == 1) {
            if (upcNumbersListForPartNumber.get(0).getAssigned() == 0) {
                warnApplicationJdbcTemplate.update(
                        updateUPCWithPartNumber_query,
                        new Object[] {partNumber, 1, partNumber}
                );
            }

        }

    }

    public void updateItemMasterWithNewUPCCodes () {
        String updateItemMasterWithNewlyCreatedUPCCodes = UPDATE_ITEM_MASTER_WITH_NEWLY_CREATED_UPC_CODES
                .replaceAll("%DB_JDE_BUSINESS%", dbApplicationConfig.getJdeBusiness().getDb())
                .replaceAll("%DB_JDE_BUSINESS_TABLE%", dbApplicationConfig.getJdeTable().getDb());

        jdeTableJdbcTemplate.execute(updateItemMasterWithNewlyCreatedUPCCodes);
    }

    public void dropf4101WkTable () {
        String dropWorkingTable = DROPF4101_WK
                .replaceAll("%DB_JDE_BUSINESS_TABLE%", dbApplicationConfig.getJdeTable().getDb());

        jdeTableJdbcTemplate.execute(dropWorkingTable);
    }
}
