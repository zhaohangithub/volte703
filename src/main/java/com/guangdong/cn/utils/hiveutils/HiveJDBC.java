package com.guangdong.cn.utils.hiveutils;

import java.sql.*;

import com.guangdong.cn.utils.GlobalConfUtils;

public class HiveJDBC {
	
	private static String driverName= GlobalConfUtils.driverName;
	private static String url = GlobalConfUtils.hiveUrl;
	private static String user = GlobalConfUtils.hiveUser;
	private static String password= GlobalConfUtils.hivePassword;
	
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;

	static {
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showDatabases() throws Exception {
		String sql = "show databases";
		System.out.println("Running: " + sql + "\n");
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			System.out.println(rs.getString(1) );
		}
	}

	public static void execute(String sql) throws Exception {
		System.out.println("Running: " + sql);
		stmt.execute(sql);
	}

	public static void select(String sql) throws Exception {
		System.out.println("Running: " + sql);
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			System.out.println(rs.getString(1) + "\t" + rs.getString(2));
		}
	}

	public static void destory() throws Exception {
		if (rs != null) {
			rs.close();
		}
		if (stmt != null) {
			stmt.close();
		}
		if (conn != null) {
			conn.close();
		}
	}

}

