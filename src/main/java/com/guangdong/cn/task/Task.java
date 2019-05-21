package com.guangdong.cn.task;

import com.guangdong.cn.app.App;
import com.guangdong.cn.utils.DateUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class Task extends TimerTask {
    private Date date;
    private int period;

    public Task(Date date, int period) {
        this.date = date;
        this.period = period;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public void run() {
        String timestamp = DateUtils.date2String(date);//执行任务所用到的时间参数
        System.out.println(timestamp);
        App.app(timestamp);//程序执行
        long time = date.getTime() + this.period;//时间相加
        date = new Date(time);//时间更新
    }
}
