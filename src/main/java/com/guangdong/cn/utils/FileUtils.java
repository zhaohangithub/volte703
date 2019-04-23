package com.guangdong.cn.utils;

import com.guangdong.cn.utils.GlobalConfUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 文件操作工具类
 * 1.解压gz文件目录
 * 2.解压zip文件目录
 * 3.获取指定目录所有文件
 * 4.解压单个gz文件成xml文件
 * 5.删除文件
 * 6.读xml文件
 * 7.set集合数据输出到文件
 * 8.解压rar文件
 */
public class FileUtils {

    /**
     * 解压gz文件夹
     * @param gzFile
     * @param outputDir
     * @param queue
     */
    public static void unTarGz(String gzFile, String outputDir, LinkedBlockingQueue<String> queue){
        TarInputStream tarIn = null;
        try{
            tarIn = new TarInputStream(new GZIPInputStream(
                    new BufferedInputStream(new FileInputStream(new File(gzFile)))));

            createDirectory(outputDir,null);//创建输出目录

            TarEntry entry = null;
            while( (entry = tarIn.getNextEntry()) != null ){

                if(entry.isDirectory()){//是目录
                    entry.getName();
                    createDirectory(outputDir,entry.getName());//创建空目录
                }else{//是文件
                    String outFile = outputDir + "/" + entry.getName();
                    File tmpFile = new File(outFile);
                    createDirectory(tmpFile.getParent() + "/",null);//创建输出目录
                    OutputStream out = null;
                    try{
                        out = new FileOutputStream(tmpFile);
                        int length = 0;
                        byte[] b = new byte[2048];
                        while((length = tarIn.read(b)) != -1){
                            out.write(b, 0, length);
                        }

                    }catch(IOException ex){
                        throw ex;
                    }finally{
                        if(out!=null)
                            out.close();
                        queue.put(outFile);
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        } finally{
            try{
                if(tarIn != null){
                    tarIn.close();
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }


    /**
     * 构建目录
     * @param outputDir
     * @param subDir
     */
    public static void createDirectory(String outputDir,String subDir){
        File file = new File(outputDir);
        if(!(subDir == null || subDir.trim().equals(""))){//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if(!file.exists()){
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }

    /**
     * 解压从ftp下载的zip文件
     * @param zipFile  指定要解压的zip文件
     * @param descDir  解压后输出的文件目录
     */
    public static void unZipFiles(String zipFile, String descDir, LinkedBlockingQueue<String> queue){
        try{
            ZipFile zip = new ZipFile(new File(zipFile), Charset.forName("utf-8"));//解决中文文件夹乱码
            //String name = zip.getName().substring(zip.getName().lastIndexOf('\\')+1, zip.getName().lastIndexOf('.'));

            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }

            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                BufferedInputStream bis = new BufferedInputStream(in);
                String outPath = (descDir+"/"+ zipEntryName).replaceAll("\\*", "/");

                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                FileOutputStream out = new FileOutputStream(outPath);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = bis.read(buf1)) > 0) {
                    bos.write(buf1, 0, len);
                }
                bos.close();
                bis.close();
                out.close();
                in.close();
                queue.put(outPath);
            }
            System.out.println("******************zip文件解压完毕********************");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据文件目录获取所有gz文件,并放入redis队列   或 queue队列
     * @param gzFileDir
     */
    public static void getFilename(String gzFileDir,LinkedBlockingQueue<String> queue){

        //Jedis jedis = JedisUtils.getJedis();
        try{
            File file = new File(gzFileDir);

            File[] files = file.listFiles();
            for (File gz : files) {
                //把每个文件路径存入redis队列
                //jedis.lpush("BigData_filename",gz.getAbsolutePath());
                //放入queue队列
                if (gz.isDirectory()){
                    getFilename(gz.getAbsolutePath(),queue);
                }else {
                    queue.put(gz.getAbsolutePath());
                }
            }
            //jedis.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 解压gz文件得到xml文件
     * @param filename
     *
     */
    public static String unGZ(String filename){
        String xmlFile = "";
        try {
            //解压gz文件的输入流
            InputStream in = new GZIPInputStream(new FileInputStream(filename));
            BufferedInputStream bis = new BufferedInputStream(in);
            //目标文件
            xmlFile = filename.substring(0,filename.length()-3);

            File OutDir = new File(xmlFile);
            byte[] buffer = new byte[1024*8];
            //输出
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(OutDir));
            int count = -1;
            while ((count = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            bis.close();
            in.close();
            bos.close();

            System.out.println(Thread.currentThread().getName()+filename+"--------解压成功");

        }catch (Exception e ){
            e.printStackTrace();
        }
        return xmlFile;

    }

    /**
     * 读取xml文件 dom方式,很耗时间
     * @param xmlFile
     */
    public  static List<Element> readXml(String xmlFile){
        SAXReader reader = new SAXReader();
        File file = new File(xmlFile);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        List<Element> elementList = null;

        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            Document document = reader.read(bis);
            elementList = document.selectNodes(GlobalConfUtils.Nodes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("File is not exsit!");
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
                try {
                    if (bis != null)
                        bis.close();
                    if (fis != null)
                        fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return elementList;
    }

    /**
     * 读xml文件 sax方式 效率较高
     * @param file
     * @return
     */
    public static List<Element> saxReadXml(String file){
        List<Element> elementList = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        saxReader.addHandler(GlobalConfUtils.SAX_Nodes, new ElementHandler() {
            @Override
            public void onEnd(ElementPath elementPath) {
                Element object = elementPath.getCurrent();
                elementList.add(object);
                object.detach();
            }

            @Override
            public void onStart(ElementPath elementPath) {

            }
        });
        try {
            saxReader.read(new BufferedInputStream(new FileInputStream(new File(file))));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return elementList;
    }

    /**
     * 删除处理完成的文件
     * @param filename 文件名
     */
    public static void delete(String filename){
        File file = new File(filename);
        if (!file.exists()){
            System.out.println("文件不存在或已被删除");
        }else {
            boolean delete = file.delete();
            /*String status = delete ? Thread.currentThread().getName()+":"+filename + "------删除成功" : filename + "============删除失败";
            System.out.println(status);*/
        }

    }

    /**
     * 将set集合中的数据输出到指定文件中
     * @param set
     */
    public static void outFile(Set<String> set){
        BufferedWriter bw = null;
        try {
            long time = System.currentTimeMillis();

            bw = new BufferedWriter(new FileWriter(GlobalConfUtils.OutFile+time+".txt"));
            // 遍历集合
            for (String s : set) {
                // 写数据
                bw.write(s);
                bw.newLine();
                bw.flush();
                System.out.println("写入文件成功--------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 释放资源
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压rar压缩文件
     * @param zipFile
     * @param destDir
     * @return
     */
    public static boolean unRAR(File zipFile, String destDir) {
        // 解决路径中存在/..格式的路径问题
        destDir = new File(destDir).getAbsoluteFile().getAbsolutePath();
        while(destDir.contains("..")) {
            String[] sepList = destDir.split("\\\\");
            destDir = "";
            for (int i = 0; i < sepList.length; i++) {
                if(!"..".equals(sepList[i]) && i < sepList.length -1 && "..".equals(sepList[i+1])) {
                    i++;
                } else {
                    destDir += sepList[i] + File.separator;
                }
            }
        }

        // 获取WinRAR.exe的路径，放在java web工程下的WebRoot路径下
        String classPath = "";
        /*try {
            classPath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        // 兼容main方法执行和javaweb下执行
        String winrarPath = (classPath.indexOf("WEB-INF") > -1 ? classPath.substring(0, classPath.indexOf("WEB-INF")) :
                classPath.substring(0, classPath.indexOf("classes"))) + "/WinRAR/WinRAR.exe";
        winrarPath = new File(winrarPath).getAbsoluteFile().getAbsolutePath();
        System.out.println(winrarPath);
*/
        boolean bool = false;
        if (!zipFile.exists()) {
            return false;
        }

        // 开始调用命令行解压，参数-o+是表示覆盖的意思
        String cmd = "E:\\tmp\\WinRaR.exe" + " X -o+ " + zipFile + " " + destDir;
        System.out.println(cmd);
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            if (proc.waitFor() != 0) {
                if (proc.exitValue() == 0) {
                    bool = false;
                }
            } else {
                bool = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("解压" + (bool ? "成功" : "失败"));
        return bool;
    }
}
