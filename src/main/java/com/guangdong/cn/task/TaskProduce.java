package com.guangdong.cn.task;

import com.guangdong.cn.utils.DateUtils;
import com.guangdong.cn.utils.GlobalConfUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskProduce {
    public static void main(String[] args) {
        Timer timer = new Timer();
        String time = GlobalConfUtils.BeginTime;//开始时间
        int period = GlobalConfUtils.Period;//间隔时间 ms单位
        Date date = DateUtils.string2Date(time);
        timer.schedule(new Task(date,period),date,period);
    }
}
