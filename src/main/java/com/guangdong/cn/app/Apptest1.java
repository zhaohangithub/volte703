package com.guangdong.cn.app;

import com.guangdong.cn.utils.FileUtils;
import com.guangdong.cn.utils.FilterUtils;
import com.guangdong.cn.pojo.MmeBean;
import com.guangdong.cn.utils.*;
import org.dom4j.Element;
import sun.net.ftp.FtpClient;

import java.util.*;
import java.util.concurrent.*;

/**
 * 最开始的驱动类,此类是把ftp服务器上满足条件的包设想为单个具体的包,而不是一个文件目录下的多个包
 * 所以该类前半部分没有使用多线程,并且ftpClint为new出来的对象,没有用到对象池
 * 该类只有在对单个xml文件处理的时候才用到多线程
 * 整体流程: 1. ftp操作,根据指定的文件去ftp上下载
 *          2. zip/gz文件解压,并将解压后的所有gz文件放入queue队列,此队列线程安全
 *          3. mme端数据准备
 *          4. 开启多线程,分别去queue队列中取文件
 *              4.1 继续解压gz文件成xml文件
 *              4.2 读取xml文件
 *              4.3 根据mme端数据进行数据的减量操作
 *              4.4 所有线程的最终结果放入set集合,此集合线程安全,并按字典序排序
 *              4.5 中间部分结果打印到console
 *          5. set集合的结果写入文件
 */
public class Apptest1 {
    //全局唯一变量--队列,存放文件地址
    static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue();
    //分组用,过滤最近的时间(未使用)
    //static ConcurrentHashMap<String,List<Element>> elementMap = new ConcurrentHashMap<String,List<Element>>();

    public static void main(String[] args) {
        //多线程写入同一个set集合,并排序
        ConcurrentSkipListSet<String> set = SetFactory.getSet();

        //1. ftp操作
        //地址
        String ftpUrl = GlobalConfUtils.FtpUrl;
        int ftpPort = GlobalConfUtils.FtpPort;
        //用户
        String ftpUsername = GlobalConfUtils.FtpUsername;
        String ftpPassword = GlobalConfUtils.FtpPassword;
        //文件下载
        String localFile = GlobalConfUtils.LocalFile;
        String ftpFile = GlobalConfUtils.FtpFile;
        //执行
        FtpClient ftpClient = FtpUtils.connectFtp(ftpUrl,ftpPort,ftpUsername,ftpPassword);
        FtpUtils.download(localFile,ftpFile,ftpClient);

        //2. zip文件解压
        //要解压的文件
        String gzOrZip = GlobalConfUtils.LocalFile;
        //就压后的存放目录
        String descDir = GlobalConfUtils.GzFileDir;
        //解压zip或gz
        if (gzOrZip.contains("zip")){
            FileUtils.unZipFiles(GlobalConfUtils.LocalFile,descDir,queue);
        }else if(gzOrZip.contains("gz")){
            FileUtils.unTarGz(GlobalConfUtils.LocalFile,descDir,queue);
        }else {
            System.out.println("文件类型不符合要求");
        }
        //将gz文件名放入queue队列中
        FileUtils.getFilename(descDir,queue);

        //3. 获取mme传递过来的数据
        //3.1 获取mme表上筛选出的MmeUeS1apId值
        List idList = new ArrayList();
        //测试值
        idList.add("269143655");
        idList.add("2953876239");
        //idList.add("806235967");

        //3.2  获取mme表usid,时间,cellid,封装成bean对象
        List beanList = new ArrayList();
        //测试值
        MmeBean bean1 = new MmeBean("7821708","269143655","2017-07-02T11:00:05.200");
        beanList.add(bean1);

        //4. 多线程,处理文件,匹配数据
        ExecutorService threadPool = Executors.newFixedThreadPool(30);

        for (int i =1;i <=30; i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //一个线程处理多个文件
                    while (queue.size() > 0){
                        try{
                            /**Jedis jedis = JedisUtils.getJedis();
                            List<String> paths = jedis.brpop(0, "BigData_filename");
                            jedis.close();
                            String filename = paths.get(1);**/
                            //1. gz文件处理
                            //获取gz文件名
                            String filename = queue.take();
                            //解压缩
                            String xmlFile = FileUtils.unGZ(filename);

                            //2. xml文件处理
                            //读取xml文件获取object元素信息
                            List<Element> elementList = FileUtils.readXml(xmlFile);

                            //删除GZ文件
                            FileUtils.delete(filename);
                            //删除xml文件
                            FileUtils.delete(xmlFile);

                            //3. 数据减量操作
                            //3.1 过滤条件1: id值过滤(测试用,生产可能不会用,直接多条件过滤)
                            System.out.println("MmeUeS1apId过滤开始--------------------------------------------");
                            List<Element> list1 = FilterUtils.idFilter2(elementList, idList);
                            FilterUtils.element2String(list1);
                            System.out.println("MmeUeS1apId过滤结束--------------------------------------------");

                            //3.2 多条件过滤(usid,cellid,endtime)
                            System.out.println("多条件过滤开始--------------------------------------------");
                            List<Element> list2 = FilterUtils.filter(list1, beanList);
                            //element解析输出
                            FilterUtils.element2String(list2);
                            System.out.println("多条件过滤结束--------------------------------------------");

                            //3.3 最大值过滤(新加一个全局的set保存所有排序好的结果,以便后续操作)
                            System.out.println("最大值过滤开始--------------------------------------------");
                            FilterUtils.maxValueFilter(list2,set);
                            /*for (String max : maxlist){
                                System.out.println(Thread.currentThread().getName()+":"+max);
                            }*/
                            System.out.println("最大值过滤结束--------------------------------------------");

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
            //将最后的set结果集写入文件中
            FileUtils.outFile(set);
            System.out.println("写入文件成功--------------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //删除zip/gz文件
            FileUtils.delete(localFile);
        }
    }

}

