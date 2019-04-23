package com.guangdong.cn.tools;

public class ContantsSQL {
    public static String SQL1 = "select * from table";
    public static String test_db = "create database test";

    //212 609 2953876239 2017-07-02T11:00:03.200 7821709
    // 28 30 19 20 38400 284 38400 468 7 26 NIL 15 0 16 0 0 0 0 0 32 NIL NIL NIL NIL NIL NIL NIL
    public static String create_MRO = "create table mro(" +
            "MmeCode String, " +
            "MmeGroupId String, " +
            "MmeUeS1apId String, " +
            "TimeStamp timestamp, " +
            "id String, " +
            "LteScRSRP int, " +
            "LteNcRSRP int, " +
            "LteScRSRQ int, " +
            "LteNcRSRQ int, " +
            "LteScEarfcn int, " +
            "LteScPci int, " +
            "LteNcEarfcn int, " +
            "LteNcPci int, " +
            "LteScTadv int, " +
            "LteScPHR int, " +
            "LteScAOA int, " +
            "LteScSinrUL int, " +
            "LteScRI1 int, " +
            "LteScRI2 int, " +
            "LteScRI4 int, " +
            "LteScRI8 int, " +
            "LteScPUSCHPRBNum int, " +
            "LteScPDSCHPRBNum int, " +
            "LteScBSR int, " +
            "LteSceNBRxTxTimeDiff int, " +
            "GsmNcellBcch int, " +
            "GsmNcellCarrierRSSI int, " +
            "GsmNcellNcc int, " +
            "GsmNcellBcc int, " +
            "TdsPccpchRSCP int, " +
            "TdsNcellUarfcn int, " +
            "TdsCellParameterId int" +
            ") row format delimited fields terminated by ' '";
    public static String loadData2mro = "load data local inpath '/export/servers/hive/1555556325686.txt' overwrite into table mro";

    public static String select1 = "select MmeUeS1apId, LteNcPci from mro";
}
