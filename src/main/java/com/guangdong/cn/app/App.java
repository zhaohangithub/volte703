package com.guangdong.cn.app;

import com.guangdong.cn.pojo.MmeBean;
import com.guangdong.cn.pools.FtpClientFactory;
import com.guangdong.cn.pools.FtpClientProperties;
import com.guangdong.cn.utils.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 目前应用的驱动类
 * 该类符合ftp目录操作,并且ftpClint对象使用了对象池
 * 该类用到两个多线程,分别是从ftp下载文件时,和xml文件处理时,并将两个多线程操作进行高度封装
 * 整体流程: 1. ftp下载文件列表准备,存入queue
 *          2. mme数据准备
 *          3. 多线程: ftp文件下载,解压后的文件名存入queue队列
 *          4. 多线程: gz文件解压,xml文件读取,object元素数据过滤,v元素过滤,结果写入set
 *          5. set结果集写入文件
 */
public class App {

    public static void main(String[] args){
        //ftp连接池
        GenericObjectPool<FTPClient> ftpClientPool = new GenericObjectPool<>(new FtpClientFactory(new FtpClientProperties()));
        ftpClientPool.setMaxIdle(30);

        //ftp下载文件队列
        LinkedBlockingQueue<String> downLoadQueue = new LinkedBlockingQueue();
        //filter操作文件队列
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue();
        //最后结果集
        ConcurrentSkipListSet<String> set = SetFactory.getSet();

        //--------------------------------------------测试begin----------------------------------------------------
        // 获取mme传递过来的数据
        //获取mme表上筛选出的MmeUeS1apId值
        List idList = new ArrayList();
        //测试值
        idList.add("46163748");
        for(int i =1; i <=1000; i++){
            idList.add(String.valueOf(i));
        }
        //idList.add("806235967");

        //获取mme表usid,时间,cellid,封装成bean对象
        List beanList = new ArrayList();
        //测试值
        MmeBean bean1 = new MmeBean("22909697","46163748","2017-09-25T20:15:05.200");
        beanList.add(bean1);
        //-----------------------------------------------测试end--------------------------------------------------

        //1. 要下载的文件名入队列
        try {
            FTPClient ftp = ftpClientPool.borrowObject();
            List<String> list = FtpUtils2.list(GlobalConfUtils.FtpPath, ftp);
            ftpClientPool.returnObject(ftp);
            //过滤(时间和mro/mre)
            for (String file : list){
                if (file.contains("MRO")){//测试
                    downLoadQueue.put(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //下载文件,文件处理,并将所有xml文件放入queue队列
        long l1 = System.currentTimeMillis();
        FilePro.fileProcess(ftpClientPool,downLoadQueue,queue);
        long l2 = System.currentTimeMillis();
        //filter过滤处理,将结果写入set------------该方法参数列表要改
        FilePro.filterProcess(queue,idList,beanList,set);

        long l3 = System.currentTimeMillis();
        //将set结果集写入文件
        FileUtils.outFile(set);
        System.out.println("文件处理时间为========================================="+String.valueOf(l2-l1));

        System.out.println("过滤时间为========================================="+String.valueOf(l3-l2));

    }
}






