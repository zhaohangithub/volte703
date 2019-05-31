package com.guangdong.cn.app;

import com.guangdong.cn.sql.ContantsSQL;
import com.guangdong.cn.utils.DateUtils;
import com.guangdong.cn.utils.hiveutils.HiveJDBC;
import org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe;

public class HiveApp {
    public static void main(String[] args) {
        try {
            HiveJDBC.execute(ContantsSQL.create_MRO);
            HiveJDBC.execute(ContantsSQL.create_MRE);
            HiveJDBC.execute(ContantsSQL.create_S1_MME);
            HiveJDBC.execute(ContantsSQL.create_VOLTE_FAILURE);
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
