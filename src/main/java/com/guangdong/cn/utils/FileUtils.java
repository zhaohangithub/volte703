package com.guangdong.cn.utils;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 文件操作工具类
 * 解压tar.gz包 解压zip包
 * 解压单文件gz zip
 * 删除文件
 * 读xml文件
 * set集合数据输出到文件
 * xml文件内容写入新文件txt csv
 * 解压rar
 */
public class FileUtils {


    /**
     * 第一次解压
     * 数据源是zip或者gz
     * 解压到指定目录并将文件名存入queue队列
     * @param gzOrZip
     * @param descDir
     * @param queue
     */
    public static void unTarGZAndZip(String gzOrZip, String descDir, LinkedBlockingQueue queue){
        if (gzOrZip.endsWith(".zip")){
            FileUtils.unZipFiles(gzOrZip, descDir,queue);
        }else if(gzOrZip.endsWith(".gz")){
            FileUtils.unTarGz(gzOrZip, descDir,queue);
        }else {
            System.out.println("文件类型不符合要求:"+gzOrZip);
        }
    }

    /**
     * 第二次解压
     * 源文件是.xml.gz或.xml.zip
     * 解压到当前目录并返回解压后的文件名
     * @param filename
     * @return
     */
    public static String unXmlGZAndZip(String filename){
        if (filename.endsWith(".gz")){
            return unXmlGZ(filename);
        }else {
            return unXmlZip(filename);
        }
    }
    /**
     * 第一次解压
     * 解压gz文件夹
     * @param gzFile
     * @param outputDir
     * @param queue
     */
    public static void unTarGz(String gzFile, String outputDir, LinkedBlockingQueue<String> queue){
        TarInputStream tarIn = null;
        try{
            tarIn = new TarInputStream(new GZIPInputStream(
                    new BufferedInputStream(new FileInputStream(gzFile))));

            createDirectory(outputDir,null);//创建输出目录

            TarEntry entry = null;
            while( (entry = tarIn.getNextEntry()) != null ){
                if(entry.isDirectory()){//是目录
                    entry.getName();
                    createDirectory(outputDir,entry.getName());//创建空目录
                }else{//是文件
                    String name = entry.getName();
                    if (name.contains("/")){//如果压缩包里包含文件夹,过滤掉文件夹,直接取最后文件
                        String[] split = name.split("/");
                        name = split[split.length - 1];
                    }
                    String outFile = outputDir  + name;
                    File tmpFile = new File(outFile);
                    if (tmpFile.exists()){//如果文件存在,将文件名存入队列,跳过
                        queue.put(outFile);
                        continue;
                    }
                    createDirectory(tmpFile.getParent() ,null);//创建输出目录
                    BufferedOutputStream out = null;
                    try{
                        out = new BufferedOutputStream(new FileOutputStream(tmpFile));
                        int length = 0;
                        byte[] b = new byte[1024 * 8];
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
            file = new File(outputDir + subDir);
        }
        if(!file.exists()){
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }

    /**
     * 第一次解压
     * zip文件
     * @param zipFile  指定要解压的zip文件
     * @param descDir  解压后输出的文件目录
     */
    public static void unZipFiles(String zipFile, String descDir, LinkedBlockingQueue<String> queue){
        try{
            ZipFile zip = new ZipFile(new File(zipFile), Charset.forName("utf-8"));//解决中文文件夹乱码

            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }

            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                BufferedInputStream bis = new BufferedInputStream(in);
                String outPath = descDir+ zipEntryName;
                File file1 = new File(outPath);
                if (file1.exists()){//如果文件存在,将文件名放入队列中,跳过
                    queue.put(outPath);
                    continue;
                }
                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(descDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                FileOutputStream out = new FileOutputStream(outPath);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                byte[] buf1 = new byte[1024 * 8];
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
            //System.out.println("******************zip文件解压完毕********************");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 第二次解压
     * xml.zip文件
     * @param file
     * @return
     */
    public static String unXmlZip(File file){
        String zipFile = file.getAbsolutePath();
        return unXmlZip(zipFile);
    }
    public static String unXmlZip(String zipFile){
        int index = zipFile.indexOf(".");
        String xmlFile = zipFile.substring(0,index) +".xml";
        File file = new File(xmlFile);
        if (file.exists()){
            return xmlFile;
        }
        try{
            ZipFile zip = new ZipFile(new File(zipFile), Charset.forName("utf-8"));//解决中文文件夹乱码

            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                BufferedInputStream bis = new BufferedInputStream(in);

                FileOutputStream out = new FileOutputStream(xmlFile);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                byte[] buf1 = new byte[1024 * 8];
                int len;
                while ((len = bis.read(buf1)) > 0) {
                    bos.write(buf1, 0, len);
                }
                bos.close();
                bis.close();
                out.close();
                in.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return xmlFile;
    }
    /**
     * 第二次解压
     * 解压xml.gz文件得到xml文件
     * @param
     *
     */
    public static String unXmlGZ(File file){
        String filename = file.getAbsolutePath();
        return unXmlGZ(filename);
    }
    public static String unXmlGZ(String file){
        int index = file.indexOf(".");
        String xmlFile = file.substring(0,index) +".xml";
        File file1 = new File(xmlFile);
        if (file1.exists()){//如果文件存在,直接返回文件名进行下一步处理
            return xmlFile;
        }
        try {//如果文件不存在,解压,并输出文件
            //解压gz文件的输入流
            InputStream in = new GZIPInputStream(new FileInputStream(file));
            BufferedInputStream bis = new BufferedInputStream(in);

            byte[] buffer = new byte[1024*8];
            //输出
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file1));
            int count = -1;
            while ((count = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            bis.close();
            in.close();
            bos.close();

        }catch (Exception e ){
            e.printStackTrace();
        }
        return xmlFile;

    }
    /**
     * 解析xml文件,返回结果集
     * @param filename
     * @return
     */
    public static List<Element> readXml(String filename){
        File file = new File(filename);
        return readXml(file);
    }
    public static List<Element> readXml(File file){
        if (!file.exists()){//如果文件不存在,返回空集合
            return new ArrayList<>();
        }
        List<Element> elementList = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        saxReader.addHandler(GlobalConfUtils.SAX_Nodes, new ElementHandler() {
            @Override
            public void onEnd(ElementPath elementPath) {
                Element object = elementPath.getCurrent();
                elementList.add(object);
                object.detach();//关闭element
            }
            @Override
            public void onStart(ElementPath elementPath) {

            }
        });
        try {
            saxReader.read(new BufferedInputStream(new FileInputStream(file)));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return elementList;
    }

    /**
     * 将set集合中的数据输出到指定文件中
     */
    public static void outFile(Collection<String> collection, String filename){
        if (collection == null || collection.size() == 0){
            return;
        }
        BufferedWriter bw = null;
        try {

            String path = "";
            if (filename.contains("MRO")){
                path = GlobalConfUtils.OutPath + "mro/";
            }
            if (filename.contains("MRE")){
                path = GlobalConfUtils.OutPath + "mre/";
            }
            File file = new File(path);
            if (!file.exists()){
                file.mkdirs();
            }
            bw = new BufferedWriter(new FileWriter(path + System.currentTimeMillis()+".csv"));
            // 遍历集合
            for (String line : collection) {
                // 写数据
                bw.write(line);
                bw.newLine();
                bw.flush();
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
     * 将map中的数据写入到csv文件中
     * @param xmlFile
     * @param map 保存数据的map,一个key,对应一个list集合,根据key去聚合
     */
    public static void outCSV(String xmlFile, Map<String, List> map){
        if (map.size() == 0 || map ==null){//如果map集合为空,直接返回
            System.err.println("map集合数据为空");
            return;
        }
        System.out.println(xmlFile);
        String name = xmlFile.substring(GlobalConfUtils.XmlPath.length(),xmlFile.length()-4);
        String path = "";
        if (name.contains("MRO")){
            path = GlobalConfUtils.OutPath + "mro/";

        }
        if (name.contains("MRE")){
            path = GlobalConfUtils.OutPath + "mre/";
        }
        File file = new File(path);
        if (!file.exists()){//如果目录不存在,创建
            file.mkdirs();
        }
        String csvFile = path+name+".csv";//文件名
        try {//否则,输出为csv文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile));
            if (xmlFile.contains("TD-LTE_MRO")) {
                for (Map.Entry<String, List> entry : map.entrySet()) {
                    List value = entry.getValue();
                    if (value.size() > 1) {//至少两部分有关联
                        StringBuffer sb2 = new StringBuffer();
                        sb2.append(entry.getKey());
                        for (Object v : value) {
                            sb2.append(v.toString());
                        }
                        bw.write(sb2.toString());
                        bw.newLine();
                        bw.flush();
                    }
                }
            }
            if (xmlFile.contains("TD-LTE_MRE")) {
                for (Map.Entry<String, List> entry : map.entrySet()) {
                    List value = entry.getValue();
                    StringBuffer sb2 = new StringBuffer();
                    sb2.append(entry.getKey());
                    if (value.size() == 1){
                        for (Object v : value){
                            sb2.append(v.toString());
                        }
                    }
                    if (value.size() > 1){
                        List<List> lists = new ArrayList<>();
                        for (int i =0; i<13; i++){
                            lists.add(new ArrayList());
                        }
                        for (Object o : value){
                            String[] split = o.toString().split(",");
                            for (int i =0;i<split.length;i++){
                                if (split[i].equals("NIL")){
                                    continue;
                                }
                                lists.get(i).add(Integer.parseInt(split[i]));
                            }
                        }
                        for (List v : lists){
                            if (v.size() ==0){
                                sb2.append("NIL,");
                            }else {
                                sb2.append(Collections.max(v)+",");
                            }
                        }
                    }
                    bw.write(sb2.toString());
                    bw.newLine();
                    bw.flush();
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据给定路径,清空文件夹
     * @param path
     */
    public static void  deleteDir(String path){
        File file = new File(path);
        if(!file.exists()){//判断是否待删除目录是否存在
            System.err.println("The dir are not exists!");
            return;
        }

        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for(String name : content){
            File temp = new File(path, name);
            if(temp.isDirectory()){//判断是否是目录
                deleteDir(temp.getAbsolutePath());//递归调用，删除目录里的内容
                temp.delete();//删除空目录
            }else{
                temp.delete();
            }
        }
    }
}
