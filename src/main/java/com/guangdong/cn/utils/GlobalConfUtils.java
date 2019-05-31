package com.guangdong.cn.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

public class GlobalConfUtils {
        public static Config load = ConfigFactory.load();

        public static String BeginTime = load.getString("BeginTime");
        public static int Period = Integer.parseInt(load.getString("Period"));

        //xml文件节点参数
        public static String SAX_Nodes = load.getString("SAX_Nodes");

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




}
