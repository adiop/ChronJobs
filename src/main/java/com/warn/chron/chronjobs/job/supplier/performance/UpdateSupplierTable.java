package com.warn.chron.chronjobs.job.supplier.performance;

public class UpdateSupplierTable {


//UPDATE SUPPLIER PERFORMANCE TABLE

    /*

    truncate table supplier_performance

    INSERT INTO [Intranet].[dbo].[supplier_performance]
    SELECT *
    FROM   Openquery([jde-dbprod],
'SELECT DISTINCT(F0101.ABAN8) , RTRIM(LTRIM(WWMLNM)) AS WWMLNM , RTRIM(LTRIM(ABAC28)) AS CC , 0 , 0 , 0 , CASE WHEN LEN(RTRIM(LTRIM(ABAC29)))>0 THEN RTRIM(LTRIM(ABAC29)) ELSE ''U'' END AS SS , RTRIM(LTRIM(ABAC30)) AS QS , CASE RTRIM(LTRIM(ABAC27)) WHEN '''' THEN ''LG'' ELSE RTRIM(LTRIM(ABAC27)) END AS BT FROM [jde-dbprod].[JDE_PRODUCTION].[PRODDTA].[F0101] INNER JOIN [jde-dbprod].[JDE_PRODUCTION].[PRODDTA].F0111 ON F0101.ABAN8 = F0111.WWAN8 AND F0111.WWIDLN=0Left JOIN [jde-dbprod].[JDE_PRODUCTION].[PRODDTA].F0116 ON F0116.ALAN8 = F0101.ABAN8 Left JOIN [jde-dbprod].[JDE_PRODUCTION].[PRODDTA].F0115 ON F0101.ABAN8 = F0115.WPAN8 WHERE  (ABAT1 = ''V'' OR ABAT1 = ''VP'')'
    )


     */
}
