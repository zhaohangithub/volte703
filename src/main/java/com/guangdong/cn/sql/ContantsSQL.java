package com.guangdong.cn.sql;

public class ContantsSQL {
    public static String dropTable = "drop table if exists mro";
    public static String showTables = "show tables";
    public static String loadData2mro = "load data local inpath '/export/servers/hive/1555556325686.txt' overwrite into table mro";
    public static String create_MRO = "create table if not exists MRO(" +
            "TimeStamp bigint, " +
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
            ") row format delimited fields terminated by ','";

    public static String create_MRE = "create table if not exists MRE(" +
            "EventType varchar(10), " +
            "TimeStamp bigint, " +
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
            ") row format delimited fields terminated by ',' ";

    public static String create_S1_MME = "create table if not exists S1_MME(" +
            "REPORT_TIME bigint, " +
            "LENGTH bigint, " +
            "CITY string, " +
            "INTERFACE string, " +
            "XDR_ID string, " +
            "RAT string, " +
            "IMSI string, " +
            "IMEI string, " +
            "MSISDN string, " +
            "PROCEDURE_TYPE bigint, " +
            "PROCEDURE_STARTTIME bigint, " +
            "PROCEDURE_ENDTIME bigint, " +
            "PROCEDURE_STATUS bigint, " +
            "REQUEST_CAUSE bigint, " +
            "FAILURE_CAUSE bigint, " +
            "KEYWORD bigint, " +
            "MME_UE_S1AP_ID bigint, " +
            "MME_GROUP_ID bigint, " +
            "MME_CODE bigint, " +
            "M_TMSI bigint, " +
            "TMSI bigint, " +
            "USER_IPV4 bigint, " +
            "USER_IPV6 string, " +
            "MME_IP_ADD string, " +
            "ENB_IP_ADD string, " +
            "MME_PORT bigint, " +
            "ENB_PORT bigint, " +
            "TAC bigint, " +
            "CELL_ID bigint, " +
            "OTHER_TAC bigint, " +
            "OTHER_ECI bigint, " +
            "APN string, " +
            "FAILUREMSG bigint, " +
            "FAILUREPROTOCOL bigint, " +
            "CAUSETYPE bigint, " +
            "PROCTIME bigint, " +
            "MAINPROC bigint, " +
            "REJECTCAUSE bigint, " +
            "SERVICETYPE bigint, " +
            "EPS_BEARER_NUMBER bigint, " +
            "ID_0 bigint, " +
            "TYPE_0 bigint, " +
            "QCI_0 bigint, " +
            "STATUS_0 bigint, " +
            "ENB_GTP_TEID_0 bigint, " +
            "SGW_GTP_TEID_0 bigint, " +
            "ID_1 bigint, " +
            "TYPE_1 bigint, " +
            "QCI_1 bigint, " +
            "STATUS_1 bigint, " +
            "ENB_GTP_TEID_1 bigint, " +
            "SGW_GTP_TEID_1 bigint, " +
            "ID_2 bigint, " +
            "TYPE_2 bigint, " +
            "QCI_2 bigint, " +
            "STATUS_2 bigint, " +
            "ENB_GTP_TEID_2 bigint, " +
            "SGW_GTP_TEID_2 bigint, " +
            "ID_3 bigint, " +
            "TYPE_3 BIGINT, " +
            "QCI_3 BIGINT, " +
            "STATUS_3 BIGINT, " +
            "ENB_GTP_TEID_3 BIGINT, " +
            "SGW_GTP_TEID_3 BIGINT, " +
            "ID_4 BIGINT, " +
            "TYPE_4 BIGINT, " +
            "QCI_4 BIGINT, " +
            "STATUS_4 BIGINT, " +
            "ENB_GTP_TEID_4 BIGINT, " +
            "SGW_GTP_TEID_4 BIGINT, " +
            "ID_5 BIGINT, " +
            "TYPE_5 BIGINT, " +
            "QCI_5 BIGINT, " +
            "STATUS_5 BIGINT, " +
            "ENB_GTP_TEID_5 BIGINT, " +
            "SGW_GTP_TEID_5 BIGINT, " +
            "ID_6 BIGINT, " +
            "TYPE_6 BIGINT, " +
            "QCI_6 BIGINT, " +
            "STATUS_6 BIGINT, " +
            "ENB_GTP_TEID_6 BIGINT, " +
            "SGW_GTP_TEID_6 BIGINT, " +
            "ID_7 BIGINT, " +
            "TYPE_7 BIGINT, " +
            "QCI_7 BIGINT, " +
            "STATUS_7 BIGINT, " +
            "ENB_GTP_TEID_7 BIGINT, " +
            "SGW_GTP_TEID_7 BIGINT, " +
            "ID_8 BIGINT, " +
            "TYPE_8 BIGINT, " +
            "QCI_8 BIGINT, " +
            "STATUS_8 BIGINT, " +
            "ENB_GTP_TEID_8 BIGINT, " +
            "SGW_GTP_TEID_8 BIGINT, " +
            "ID_9 BIGINT, " +
            "TYPE_9 BIGINT, " +
            "QCI_9 BIGINT, " +
            "STATUS_9 BIGINT, " +
            "ENB_GTP_TEID_9 BIGINT, " +
            "SGW_GTP_TEID_9 BIGINT, " +
            "ID_10 BIGINT, " +
            "TYPE_10 BIGINT, " +
            "QCI_10 BIGINT, " +
            "STATUS_10 BIGINT, " +
            "ENB_GTP_TEID_10 BIGINT, " +
            "SGW_GTP_TEID_10 BIGINT, " +
            "ID_11 BIGINT, " +
            "TYPE_11 BIGINT, " +
            "QCI_11 BIGINT, " +
            "STATUS_11 BIGINT, " +
            "ENB_GTP_TEID_11 BIGINT, " +
            "SGW_GTP_TEID_11 BIGINT, " +
            "ID_12 BIGINT, " +
            "TYPE_12 BIGINT, " +
            "QCI_12 BIGINT, " +
            "STATUS_12 BIGINT, " +
            "ENB_GTP_TEID_12 BIGINT, " +
            "SGW_GTP_TEID_12 BIGINT, " +
            "ID_13 BIGINT, " +
            "TYPE_13 BIGINT, " +
            "QCI_13 BIGINT, " +
            "STATUS_13 BIGINT, " +
            "ENB_GTP_TEID_13 BIGINT, " +
            "SGW_GTP_TEID_13 BIGINT, " +
            "ID_14 BIGINT, " +
            "TYPE_14 BIGINT, " +
            "QCI_14 BIGINT, " +
            "STATUS_14 BIGINT, " +
            "ENB_GTP_TEID_14 BIGINT, " +
            "SGW_GTP_TEID_14 BIGINT, " +
            "ID_15 BIGINT, " +
            "TYPE_15 BIGINT, " +
            "QCI_15 BIGINT, " +
            "STATUS_15 BIGINT, " +
            "ENB_GTP_TEID_15 BIGINT, " +
            "SGW_GTP_TEID_15 BIGINT, " +
            "SGW_IP_ADD STRING, " +
            "PGW_ADDRESS STRING, " +
            "DNS_IP_ADD STRING, " +
            "OTHER_RAT BIGINT, " +
            "UE_AMBR BIGINT, " +
            "ENODEB_UE_S1AP_ID BIGINT, " +
            "GUTI_MAPPED BIGINT, " +
            "OLD_MME_GROUP_ID BIGINT, " +
            "OLD_MME_CODE BIGINT, " +
            "OLD_TAC BIGINT, " +
            "NEW_LAC BIGINT, " +
            "KEYWORD3 STRING, " +
            "KEYWORD4 STRING, " +
            "ARP_0 BIGINT, " +
            "MBRDL_0 BIGINT, " +
            "MBRUL_0 BIGINT, " +
            "GBRDL_0 BIGINT, " +
            "GBRUL_0 BIGINT, " +
            "ARP_1 BIGINT, " +
            "MBRDL_1 BIGINT, " +
            "MBRUL_1 BIGINT, " +
            "GBRDL_1 BIGINT, " +
            "GBRUL_1 BIGINT, " +
            "ARP_2 BIGINT, " +
            "MBRDL_2 BIGINT, " +
            "MBRUL_2 BIGINT, " +
            "GBRDL_2 BIGINT, " +
            "GBRUL_2 BIGINT, " +
            "ARP_3 BIGINT, " +
            "MBRDL_3 BIGINT, " +
            "MBRUL_3 BIGINT, " +
            "GBRDL_3 BIGINT, " +
            "GBRUL_3 BIGINT, " +
            "ARP_4 BIGINT, " +
            "MBRDL_4 BIGINT, " +
            "MBRUL_4 BIGINT, " +
            "GBRDL_4 BIGINT, " +
            "GBRUL_4 BIGINT, " +
            "ARP_5 BIGINT, " +
            "MBRDL_5 BIGINT, " +
            "MBRUL_5 BIGINT, " +
            "GBRDL_5 BIGINT, " +
            "GBRUL_5 BIGINT, " +
            "ARP_6 BIGINT, " +
            "MBRDL_6 BIGINT, " +
            "MBRUL_6 BIGINT, " +
            "GBRDL_6 BIGINT, " +
            "GBRUL_6 BIGINT, " +
            "ARP_7 BIGINT, " +
            "MBRDL_7 BIGINT, " +
            "MBRUL_7 BIGINT, " +
            "GBRDL_7 BIGINT, " +
            "GBRUL_7 BIGINT, " +
            "ARP_8 BIGINT, " +
            "MBRDL_8 BIGINT, " +
            "MBRUL_8 BIGINT, " +
            "GBRDL_8 BIGINT, " +
            "GBRUL_8 BIGINT, " +
            "ARP_9 BIGINT, " +
            "MBRDL_9 BIGINT, " +
            "MBRUL_9 BIGINT, " +
            "GBRDL_9 BIGINT, " +
            "GBRUL_9 BIGINT, " +
            "ARP_10 BIGINT, " +
            "MBRDL_10 BIGINT, " +
            "MBRUL_10 BIGINT, " +
            "GBRDL_10 BIGINT, " +
            "GBRUL_10 BIGINT, " +
            "ARP_11 BIGINT, " +
            "MBRDL_11 BIGINT, " +
            "MBRUL_11 BIGINT, " +
            "GBRDL_11 BIGINT, " +
            "GBRUL_11 BIGINT, " +
            "ARP_12 BIGINT, " +
            "MBRDL_12 BIGINT, " +
            "MBRUL_12 BIGINT, " +
            "GBRDL_12 BIGINT, " +
            "GBRUL_12 BIGINT, " +
            "ARP_13 BIGINT, " +
            "MBRDL_13 BIGINT, " +
            "MBRUL_13 BIGINT, " +
            "GBRDL_13 BIGINT, " +
            "GBRUL_13 BIGINT, " +
            "ARP_14 BIGINT, " +
            "MBRDL_14 BIGINT, " +
            "MBRUL_14 BIGINT, " +
            "GBRDL_14 BIGINT, " +
            "GBRUL_14 BIGINT, " +
            "ARP_15 BIGINT, " +
            "MBRDL_15 BIGINT, " +
            "MBRUL_15 BIGINT, " +
            "GBRDL_15 BIGINT, " +
            "GBRUL_15 BIGINT, " +
            "CITYOID STRING, " +
            "IOTGROUPCODE BIGINT, " +
            "IOTSUBCODE BIGINT, " +
            "IOTFUNCCODE BIGINT, " +
            "CITYNAME STRING, " +
            "SLICETIME STRING" +
            ") row format delimited fields terminated by ','";

    public static String create_VOLTE_FAILURE = "create table if not exists VOLTE_FAILURE(" +
            "VOLTE_FROM String, " +
            "PROV int, " +
            "CITY int, " +
            "IMSI char(20), " +
            "IMEI char(20), " +
            "MSISDN char(20), " +
            "CALLING char(20), " +
            "CALLED char(20), " +
            "SERVICE_TYPE int, " +
            "START_ECI int, " +
            "BEGIN_TIME bigint, " +
            "END_TIME bigint, " +
            "RESULT int, " +
            "ALERT_RESULT int, " +
            "ERROR_CODE int, " +
            "CALL_RESULT int, " +
            "REG_RESULT int, " +
            "ALERT_DELAY int, " +
            "TALK_LEN int, " +
            "LOCAL_TYPE int, " +
            "REMOTE_TYPE int, " +
            "VIDEO_STATE int, " +
            "DROP_TYPE int, " +
            "CMCC_DROP_TYPE int, " +
            "SIPFRI_NE_TYPE int, " +
            "SIPFRI_MSG int, " +
            "DECISION_NE int, " +
            "SIPFRI_WARNING_CODE int, " +
            "SIPFRI_WARNING_TEXT char(64), " +
            "SIPFRI_Q850_CAUSE int, " +
            "SIPFRI_Q850_TEXT char(64), " +
            "SIPFRI_SIP_CAUSE int, " +
            "SIPFRI_SIP_TEXT char(64), " +
            "DECISION_RESULT varchar(255), " +
            "S1_CAUSE1_TAC int, " +
            "S1_CAUSE1_ECI int, " +
            "S1_CAUSE1_VALS int, " +
            "S1_CAUSE1_NAME int, " +
            "USER_AGENT char(64), " +
            "IMEI_FACTORY_ID int, " +
            "IMEI_MODEL_ID int, " +
            "ROAMING_STATE int, " +
            "ECI_SCENE int, " +
            "ORG_CALLED String, " +
            "F_ECI int, " +
            "DRA_ABORT_CAUSE int, " +
            "DRA_ASR_CCR_CAUSE int, " +
            "DRA_ASR_RAT_TYPE int, " +
            "DRA_ASR_S1_CAUSE int, " +
            "DECISION_SCENARIO String, " +
            "DECISION_ADVISE String, " +
            "ABNORMAL_TYPE int, " +
            "V2V_CALL_FLAG int, " +
            "SV_CAUSE int, " +
            "S1_CAUSE1_DELAY int, " +
            "DRA_ASR_CCR_DELAY int, " +
            "DRA_ASR_SIP_DELAY int, " +
            "TARGET_CELL_ID bigint, " +
            "REQUEST_RESULT int" +
            ") row format delimited fields terminated by ','";

    //获取S1_MME减量数据
    public static String getDecrement_S1_MME = "SELECT s.* FROM s1_mme s,volte_failure f " +
            "WHERE s.imsi=f.imsi AND (s.procedure_starttime BETWEEN f.end_time-20000 AND f.end_time+20000 " +
            "or s.procedure_endtime BETWEEN f.end_time-20000 AND f.end_time+20000)";




    public static String selectMRO = "with MRO_N " +
            "as " +
            "(select id,mmeues1apid, time_difference from " +
            "(select *, " +
            "(unix_timestamp() - unix_timestamp(`timestamp`)) as time_difference, " +
            "row_number() over(" +
            "partition by id " +
            "order by (unix_timestamp() - unix_timestamp(`timestamp`)))rank from mro " +
            "where (unix_timestamp(`timestamp`) < unix_timestamp()))a  where a.rank =1), " +
            "MRO_N_1 " +
            "as " +
            "(select id,mmeues1apid, time_difference from " +
            "(select *, " +
            "(unix_timestamp(`timestamp`) - unix_timestamp()) as time_difference, " +
            "row_number() over(" +
            "partition by id " +
            "order by (unix_timestamp(`timestamp`) - unix_timestamp()))rank from mro  " +
            "where (unix_timestamp(`timestamp`) < unix_timestamp()))a  where a.rank =1) " +
            "select MRO_N.*,case when MRO_N.mmeues1apid=MRO_N_1.mmeues1apid then '同区' else '不同区' end " +
            "from MRO_N,MRO_N_1 where MRO_N.id=MRO_N_1.id and MRO_N.mmeues1apid=MRO_N_1.mmeues1apid " +
            "or (MRO_N.mmeues1apid != MRO_N_1.mmeues1apid and MRO_N.time_difference < MRO_N_1.time_difference) " +
            "union all  " +
            "select MRO_N_1.*,'不同区' from MRO_N,MRO_N_1 where MRO_N.id=MRO_N_1.id and MRO_N.mmeues1apid != MRO_N_1.mmeues1apid " +
            "and MRO_N.time_difference > MRO_N_1.time_difference";
}
