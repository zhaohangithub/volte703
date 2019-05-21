package com.guangdong.cn.process;

import com.guangdong.cn.utils.FileUtils;
import com.guangdong.cn.utils.FilterUtils;
import com.guangdong.cn.utils.FtpUtils;
import com.guangdong.cn.utils.GlobalConfUtils;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.dom4j.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Process {
    /**
     * 文件处理过程
     * 1. ftp文件下载
     * 2. gz/zip文件解压成gz文件
     * @param ftpClientPool ftp连接池
     * @param downLoadQueue 下载文件列表队列
     * @param queue gz文件列表队列
     */
    public static void ftpProcess(GenericObjectPool<FTPClient> ftpClientPool,
                                   LinkedBlockingQueue<String> downLoadQueue,
                                   LinkedBlockingQueue<String> queue){
        if (downLoadQueue.size() == 0){//如果没有匹配到相应文件,返回
            System.err.println("ftp服务器没有对应文件");
            return;
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        for (int i=1;i <=5; i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    while (downLoadQueue.size() > 0){
                        try {
                            //获取文件名
                            String fileName = downLoadQueue.poll();
                            //ftp客户端去下载指定文件
                            FTPClient ftpClient = ftpClientPool.borrowObject();
                            boolean connection = FtpUtils.downLoadFile(ftpClient, GlobalConfUtils.FtpPath, fileName, GlobalConfUtils.DownloadPath);
                            if (!connection){//如果下载过程中未获得连接,文件未处理
                                downLoadQueue.put(fileName);//将文件重新加入队列
                            }
                            ftpClientPool.returnObject(ftpClient);
                            //2. zip/gz文件解压
                            //要解压的文件
                            String gzOrZip = GlobalConfUtils.DownloadPath + fileName;
                            //解压后的存放目录
                            String descDir = GlobalConfUtils.XmlPath;
                            //解压zip或gz
                            FileUtils.unTarGZAndZip(gzOrZip,descDir,queue);
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        try {
            threadPool.shutdown();
            threadPool.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 文件读取,过滤过程
     * 1.解压gz文件成xml
     * @param ftpClientPool
     * @param queue  存储文件的队列
     * @param list  获取所需数据需要的字段list
     */
    public static void fileProcess(GenericObjectPool<FTPClient> ftpClientPool, LinkedBlockingQueue<String> queue, List<String> list){
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        if (queue.size() == 0){//如果没有匹配到相应文件,返回
            System.err.println("MRO/MRE没有对应文件");
            return;
        }
        for (int i =1;i <=10; i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //一个线程处理多个文件
                    while (queue.size() > 0){
                        try{
                            //获取gz/zip文件名
                            String filenameAndCellIds = queue.poll();
                            String filename = filenameAndCellIds.split(",")[0];

                            String xmlFile = FileUtils.unXmlGZAndZip(filename);//解压缩得到xml文件

                            List<Element> elementList = FileUtils.readXml(xmlFile);//读取xml文件获取element信息

                            MultiValueMap multiValueMap = FilterUtils.fieldFilter(filenameAndCellIds,elementList, list);//处理xml数据,返回map

                            FileUtils.outCSV(xmlFile, multiValueMap);//写入文件,一对一
                            //文件上传
//                            FTPClient ftpClient = ftpClientPool.borrowObject();
//                            String ftpPath = GlobalConfUtils.FtpPath;
//                            FtpUtils.upLoadFile(ftpClient,ftpPath,csv);
//                            ftpClientPool.returnObject(ftpClient);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        try {
            threadPool.shutdown();
            threadPool.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件清除流程
     */
    public static void deleteProcess(){
        //清空压缩包和xml文件
        System.out.println("是否删除文件:yes/no");
        for (int i = 1; i <=3;i++){
            Scanner sc = new Scanner(System.in);
            String command = sc.next();
            if (command.equalsIgnoreCase("yes")) {
                System.out.println("文件清理开始-------------------");
                FileUtils.deleteDir(GlobalConfUtils.XmlPath);
                break;
            }
            if (command.equalsIgnoreCase("no")){
                System.out.println("不删除文件");
                break;
            }
            if (i <3)
                System.out.println("输入指令错误,请重新输入:yes/no");
            else System.out.println("3次指令输入错误,程序自动退出");
        }
    }
}

