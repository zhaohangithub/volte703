package com.guangdong.cn.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

public class GlobalConfUtils {
        public static Config load = ConfigFactory.load();

        public static String BeginTime = load.getString("BeginTime");
        public static int Period = Integer.parseInt(load.getString("Period"));
        //xml文件操作
        public static String valueElement = load.getString("ValueElement");
        public static String Nodes = load.getString("Nodes");
        public static String SAX_Nodes = load.getString("SAX_Nodes");
        public static int HW_Index = Integer.parseInt(load.getString("HW_Index"));
        public static String MmeUeS1apId = load.getString("MmeUeS1apId");
        public static String CellId = load.getString("CellId");
        public static String TimeStamp = load.getString("TimeStamp");

        //ftp连接池
        public static String FtpUrl = load.getString("FtpUrl");
        public static int FtpPort = Integer.parseInt(load.getString("FtpPort"));
        public static String FtpUsername = load.getString("FtpUsername");
        public static String FtpPassword = load.getString("FtpPassword");
        public static String FtpPath = load.getString("FtpPath");

        //文件操作
        public static String DownloadPath = load.getString("DownloadPath");
        public static String XmlPath = load.getString("XmlPath");
        public static String OutPath = load.getString("OutPath");

        //文件输出
        public static String MROColumn = load.getString("MROColumn");
        public static String MREColumn = load.getString("MREColumn");

        //文件行数
        public static int RowNumber = Integer.parseInt(load.getString("RowNumber"));

        //hive
        public static String driverName = load.getString("driverName");
        public static String hiveUrl = load.getString("hiveUrl");
        public static String hiveUser = load.getString("hiveUser");
        public static String hivePassword = load.getString("hivePassword");

        //hbase
        public static String ZookeeperQuorum = load.getString("ZookeeperQuorum");
        public static String ZookeeperPort = load.getString("ZookeeperPort");
        public static String Hmaster = load.getString("Hmaster");


        //redis
        public static String RedisHost = load.getString("RedisHost");
        public static String RedisPort = load.getString("RedisPort");


}
