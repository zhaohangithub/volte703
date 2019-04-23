package com.guangdong.cn.pojo;

import java.io.Serializable;

public class MmeBean implements Serializable {
    private String IMSI;
    private String Cell_Id;
    private String MME_UE_S1AP_ID;
    private String End_Time;

    public MmeBean() {
    }

    public MmeBean(String cell_Id, String MME_UE_S1AP_ID, String end_Time) {
        Cell_Id = cell_Id;
        this.MME_UE_S1AP_ID = MME_UE_S1AP_ID;
        End_Time = end_Time;
    }

    public String getCell_Id() {
        return Cell_Id;
    }

    public void setCell_Id(String cell_Id) {
        Cell_Id = cell_Id;
    }

    public String getMME_UE_S1AP_ID() {
        return MME_UE_S1AP_ID;
    }

    public void setMME_UE_S1AP_ID(String MME_UE_S1AP_ID) {
        this.MME_UE_S1AP_ID = MME_UE_S1AP_ID;
    }

    public String getEnd_Time() {
        return End_Time;
    }

    public void setEnd_Time(String end_Time) {
        End_Time = end_Time;
    }

    @Override
    public String toString() {
        return "MmeBean{" +
                "Cell_Id='" + Cell_Id + '\'' +
                ", MME_UE_S1AP_ID='" + MME_UE_S1AP_ID + '\'' +
                ", End_Time='" + End_Time + '\'' +
                '}';
    }
}
