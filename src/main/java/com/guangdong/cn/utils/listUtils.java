package com.guangdong.cn.utils;

import java.util.ArrayList;
import java.util.List;

public class listUtils {
    //获取数据所需字段
    public static List<String>  getList1(){
        List<String> list = new ArrayList<>();
        //第一部分
        list.add("EventType");
        list.add("TimeStamp");
        list.add("id");
        list.add("MmeUeS1apId");
        list.add("MR.LteScEarfcn");
        list.add("MR.LteScPci");
        list.add("MR.LteScRSRP");
        list.add("MR.LteScRSRQ");
        list.add("MR.LteScTadv");
        list.add("MR.LteScPHR");
        list.add("MR.LteScAOA");
        list.add("MR.LteScSinrUL");
        list.add("MR.LteScPUSCHPRBNum");
        list.add("MR.LteScPDSCHPRBNum");
        list.add("MR.LteSceNBRxTxTimeDiff");
        //第二部分
        list.add("MR.LteScPlrULQci1");
        list.add("MR.LteScPlrULQci2");
        list.add("MR.LteScPlrDLQci1");
        list.add("MR.LteScPlrDLQci2");
        //第三部分
        list.add("MR.LteScRIP");
        //最大值部分 和第一部分一块处理
        list.add("MR.LteNcEarfcn");
        list.add("MR.LteNcRSRP");
        list.add("MR.LteNcPci");
        list.add("MR.GsmNcellCarrierRSSI");
        list.add("MR.GsmNcellBcch");
        list.add("MR.GsmNcellNcc");
        list.add("MR.GsmNcellBcc");
        return list;
    }

    public static List<String> cellIds(){
        //文件过滤,根据cellIds
        List<String> cellIds = new ArrayList<>();
        cellIds.add("22909697");
        cellIds.add("22909699");
        cellIds.add("48030209");
        cellIds.add("51970949");
        cellIds.add("51971716");
        cellIds.add("22291201");
        cellIds.add("22291202");
        cellIds.add("43370241");
        cellIds.add("22326529");
        return cellIds;
    }

}
