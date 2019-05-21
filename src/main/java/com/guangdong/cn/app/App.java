package com.guangdong.cn.app;

import com.guangdong.cn.pools.FtpClientFactory;
import com.guangdong.cn.pools.FtpClientProperties;
import com.guangdong.cn.process.Process;
import com.guangdong.cn.utils.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.*;
import java.util.concurrent.*;

/**
 * 该驱动的作用是:根据si_mme端传输过来的cellid集合,过滤从ftp下载下来的mro(mre)数据,并输出指定格式
 *
 * 该类符合ftp目录操作,并且ftpClint对象使用了对象池
 * 该类用到两个多线程,分别是从ftp下载文件时,和xml文件处理时,并将两个多线程操作进行高度封装
 * 整体流程: 1. ftp下载文件列表准备,存入queue
 *          2. mme(cellids)数据准备
 *          3. 多线程: ftp文件下载
 *          4. 多线程: gz文件解压,xml文件读取,解析按指定字段格式存入map集合
 *          5. map结果集写入csv文件
 */
public class App {
    public static void app(String timestamp){
        System.out.println("begin------------");

        GenericObjectPool<FTPClient> ftpClientPool = new GenericObjectPool<>(new FtpClientFactory(new FtpClientProperties()));//ftp连接池

        LinkedBlockingQueue<String> ftpQueue = new LinkedBlockingQueue();//ftp文件名队列
        LinkedBlockingQueue<String> xmlQueue = new LinkedBlockingQueue();//xml文件名队列

        List<String> list1 = listUtils.getList1();//获取数据需要的字段list构造
        List<String> cellIds = listUtils.cellIds();//cellID集合

        FilterUtils.ftpFilter(ftpClientPool,ftpQueue,timestamp);//1. 要下载的文件名过滤后存入队列
        Process.ftpProcess(ftpClientPool,ftpQueue,xmlQueue);//2. 多线程:下载文件并将所有xml文件放入queue队列
        LinkedBlockingQueue<String> queue = FilterUtils.fileFilter(xmlQueue, cellIds);//3. 文件名过滤
        Process.fileProcess(ftpClientPool,queue,list1);//4. 多线程:xml文件解析,写入csv文件

        //Process.deleteProcess();//文件清除
        System.out.println("end----------------");
    }
}