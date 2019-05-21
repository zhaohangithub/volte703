package com.guangdong.cn.sql;

public class ContantsSQL {
    public static String SQL1 = "select * from table";
    public static String test_db = "create database if not exists test";

    public static String create_MRO = "create table if not exists mro(" +
            "TimeStamp timestamp, " +
            "id int, " +
            "MmeUeS1apId bigint, " +
            "MRLteScEarfcn int, " +
            "MRLteScPci int, " +
            "MRLteScRSRP int, " +
            "MRLteScRSRQ int, " +
            "MRLteScTadv int, " +
            "MRLteScPHR int, " +
            "MRLteScAOA int, " +
            "MRLteScSinrUL int, " +
            "MRLteScPUSCHPRBNum int, " +
            "MRLteScPDSCHPRBNum int, " +
            "MRLteSceNBRxTxTimeDiff int, " +
            "MRO_F_MAXSMRLteNcRSRP int, " +
            "MRO_F_MAXSMRLteNcPci int, " +
            "MRO_F_MAXNMRLteNcRSRP int, " +
            "MRO_F_MAXNMRLteNcPci int, " +
            "MRO_F_MAXNMRLteNcEarfcn int, " +
            "MRO_F_MAXMRGsmNcellCarrierRSSI int, " +
            "MRO_F_MAXMRGsmNcellBcch int, " +
            "MRO_F_MAXMRGsmNcellNcc int, " +
            "MRO_F_MAXMRGsmNcellBcc int, " +
            "MRLteScPlrULQci1 int, " +
            "MRLteScPlrULQci2 int, " +
            "MRLteScPlrDLQci1 int, " +
            "MRLteScPlrDLQci2 int, " +
            "MRLteScRIP int" +
            ") row format delimited fields terminated by ',' " +
            "STORED AS TEXTFILE ";

    public static String create_MRE = "create table if not exists mre(" +
            "EventType varchar(10), " +
            "TimeStamp timestamp, " +
            "id int, " +
            "MmeUeS1apId bigint, " +
            "MRLteScEarfcn int, " +
            "MRLteScPci int, " +
            "MRLteScRSRP int, " +
            "MRLteScRSRQ int, " +
            "MRO_F_MAXSMRLteNcRSRP int, " +
            "MRO_F_MAXSMRLteNcPci int, " +
            "MRO_F_MAXNMRLteNcRSRP int, " +
            "MRO_F_MAXNMRLteNcPci int, " +
            "MRO_F_MAXNMRLteNcEarfcn int, " +
            "MRO_F_MAXMRGsmNcellCarrierRSSI int, " +
            "MRO_F_MAXMRGsmNcellBcch int, " +
            "MRO_F_MAXMRGsmNcellNcc int, " +
            "MRO_F_MAXMRGsmNcellBcc int" +
            ") row format delimited fields terminated by ',' " +
            "STORED AS TEXTFILE";
    public static String loadData2mro = "load data local inpath '/export/servers/hive/1555556325686.txt' overwrite into table mro";
    public static String loadData2mro1 = "load data inpath '/user/myhue/TD-LTE_MRO_HUAWEI_188002145143_187618_20170925201500.csv' overwrite into table mro";

    public static String dropTable = "drop table if exists mro1";
    public static String select1 = "select * from mro limit 5";
    public static String select = "select *,unix_timestamp(`timestamp`) - unix_timestamp(`endtime`) " +
            "case when unix_timestamp(`timestamp`) - unix_timestamp(`endtime`) > 0 then " +
            "row_number() over(partition by f.imsi order by m.timestamp-f.endtime ) as time1 where time1 <=1 " +
            "from mro m,failure f " +
            "where f.imsi=m.imsi and m.timestamp between f.endtime-20000 and f.endtime+20000" +
            "group by imsi " +
            "";

}
