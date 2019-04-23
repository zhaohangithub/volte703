package com.guangdong.cn.app.test_demo;

import com.guangdong.cn.tools.ContantsSQL;
import com.guangdong.cn.utils.hiveutils.HiveJDBC;

public class HiveApp {
    public static void main(String[] args) {
        try {
           //HiveJDBC.createTable(ContantsSQL.create_MRO);
            HiveJDBC.loadData(ContantsSQL.loadData2mro);
 //           HiveJDBC.selectData(ContantsSQL.select1);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                HiveJDBC.destory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
