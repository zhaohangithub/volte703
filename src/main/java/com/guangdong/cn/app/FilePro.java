package com.guangdong.cn.app;

import com.guangdong.cn.utils.FileUtils;
import com.guangdong.cn.utils.FilterUtils;
import com.guangdong.cn.utils.FtpUtils2;
import com.guangdong.cn.utils.GlobalConfUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.dom4j.Element;
import org.jruby.RubyProcess;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class FilePro {

    /**
     * 文件处理过程
     * 1. ftp文件下载
     * 2. gz/zip文件解压成gz文件
     *
     * @param ftpClientPool ftp连接池
     * @param downLoadQueue 下载文件列表队列
     * @param queue gz文件列表队列
     */
    public static void fileProcess(GenericObjectPool<FTPClient> ftpClientPool,
                                   LinkedBlockingQueue<String> downLoadQueue,
                                   LinkedBlockingQueue<String> queue){

        ExecutorService threadPool = Executors.newFixedThreadPool(30);
        for (int i=1;i <=30; i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    while (downLoadQueue.size() > 0){
                        try {
                            //获取文件名
                            String fileName = downLoadQueue.take();

                            //ftp客户端去下载指定文件
                            FTPClient ftpClient = ftpClientPool.borrowObject();
                            FtpUtils2.downLoadFile(ftpClient, GlobalConfUtils.FtpPath,fileName,GlobalConfUtils.LocalPath);
                            ftpClientPool.returnObject(ftpClient);

                            //2. zip/gz文件解压
                            //要解压的文件
                            String gzOrZip = GlobalConfUtils.LocalPath + fileName;
                            //解压后的存放目录
                            String descDir = GlobalConfUtils.GzFileDir;
                            //解压zip或gz
                            if (gzOrZip.contains("zip")){
                                FileUtils.unZipFiles(gzOrZip, descDir,queue);
                            }else if(gzOrZip.contains("gz")){
                                FileUtils.unTarGz(gzOrZip, descDir,queue);
                            }else {
                                System.out.println("文件类型不符合要求");
                            }

                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        }

        try {
            threadPool.shutdown();
            threadPool.awaitTermination(3, TimeUnit.MINUTES);
            /*Iterator<String> iterator = queue.iterator();
            while (iterator.hasNext()){
                System.out.println(iterator.next().toString());
            }*/
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 文件读取,过滤过程
     * 1.解压gz文件成xml
     * @param queue  存储文件的队列
     * @param idList s1_mme端传过来的数据
     * @param beanList s1_mme端传过来的数据
     * @param set 最后符合条件的数据集存储到set集合中,单例,线程安全
     */
    public static void filterProcess(LinkedBlockingQueue<String> queue,
                                     List idList,
                                     List beanList,
                                     ConcurrentSkipListSet<String> set){

        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i =1;i <=10; i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //一个线程处理多个文件
                    while (queue.size() > 0){
                        try{
                            //1. gz文件处理
                            //获取gz文件名
                            String filename = queue.take();
                            //解压缩
                            long l1 = System.currentTimeMillis();
                            String xmlFile = FileUtils.unGZ(filename);
                            long l2 = System.currentTimeMillis();
                            System.out.println("解压Gz所用时间=========="+ String.valueOf(l2 - l1));
                            //2. xml文件处理
                            //读取xml文件获取object元素信息

                            List<Element> elementList = FileUtils.saxReadXml(xmlFile);
                            long l3 = System.currentTimeMillis();
                            System.out.println("读xml文件所用时间========="+ String.valueOf(l3 - l2));
                            //删除GZ文件

                            FileUtils.delete(filename);
                            //删除xml文件
                            FileUtils.delete(xmlFile);
                            long l4 = System.currentTimeMillis();
                            System.out.println("删除文件时间========="+ String.valueOf(l4 - l3));
                            //3. 数据减量操作
                            //3.1 过滤条件1: id值过滤(测试用,生产可能不会用,直接多条件过滤)
                            //System.out.println(Thread.currentThread().getName()+"MmeUeS1apId过滤开始--------------------------------------------");
                            //long l1 = System.currentTimeMillis();
                            List<Element> list1 = FilterUtils.idFilter2(elementList, idList);
                            long l5 = System.currentTimeMillis();
                            System.out.println("id过滤时间========="+ String.valueOf(l5 - l4));
                            //List<String> stringList1 = FilterUtils.element2String(list1);
                           /* for (String v : stringList1){
                                System.out.println(v);
                            }
                            System.out.println(Thread.currentThread().getName()+"MmeUeS1apId过滤结束--------------------------------------------");
                            System.out.println("id过滤时间为:"+String.valueOf(l2-l1));*/
                            //3.2 多条件过滤(usid,cellid,endtime)
                            //System.out.println(Thread.currentThread().getName()+"多条件过滤开始--------------------------------------------");
                            List<Element> list2 = FilterUtils.filter(list1, beanList);
                            long l7 = System.currentTimeMillis();
                            System.out.println("多条件过滤时间========="+ String.valueOf(l7 - l5));
                            //element解析输出
//                            List<String> stringList2 = FilterUtils.element2String(list2);
//                            for (String v : stringList2){
//                                System.out.println(v);
//                            }
//                            System.out.println(Thread.currentThread().getName()+"多条件过滤结束--------------------------------------------");

                            //3.3 最大值过滤(新加一个全局的set保存所有排序好的结果,以便后续操作)
                            //System.out.println(Thread.currentThread().getName()+"最大值过滤开始--------------------------------------------");
                            FilterUtils.maxValueFilter(list2,set);

                            long l6 = System.currentTimeMillis();
                            System.out.println("最大值过滤时间========="+ String.valueOf(l6 - l5));
                            //展示结果,实际存入set集合
//                            for (String max : maxlist){
//                                System.out.println(Thread.currentThread().getName()+":"+max);
//                            }
//                            System.out.println(Thread.currentThread().getName()+"最大值过滤结束-------------------------------");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        try {
            threadPool.shutdown();
            threadPool.awaitTermination(3, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

