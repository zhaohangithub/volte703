package com.guangdong.cn.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FastDateParser;

import java.text.ParseException;
import java.util.Date;

public class DateUtils {
    //线程安全的dateformat
    private static final DateParser DATE_PARSER = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSS");

    //字符串转时间戳
    public static long date2timestamp(String date){
        long timestamp = 0;

        if(!StringUtils.isEmpty(date)){
            try {
                timestamp = DATE_PARSER.parse(date).getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return  timestamp;
    }
}
