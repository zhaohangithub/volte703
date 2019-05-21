package com.guangdong.cn.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FastDateParser;

import java.text.ParseException;
import java.util.Date;

public class DateUtils {
    //线程安全的dateformat
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss");

    //日期转字符串
    public static String date2String(Date date){
        return DATE_FORMAT.format(date);
    }

    //字符串转日期
    public static Date string2Date(String date){
        Date date1 = null;
        try {
            date1 = DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }
}