package com.guangdong.cn.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;  
import org.apache.commons.net.ftp.FTPClient;  
import org.apache.commons.net.ftp.FTPFile;  
import org.apache.commons.net.ftp.FTPReply;

import org.apache.log4j.Logger;

/** 
 * 操作FTP服务器上指定目录下面满足条件的所有文件
 */  
public class FtpUtils2 {

    /**
     *连接
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     */
    public static FTPClient login(String host,int port,String username,String password){
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(host,port);
            ftp.login(username,password);
            ftp.setControlEncoding("utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftp;

    }  
      
    /** 
     * 关闭数据链接 
     * @throws IOException 
     */  
    public static void disConnection(FTPClient ftp) throws IOException{
        if(ftp.isConnected()){
            ftp.disconnect();
        }  
    }  
      
    /** 
     * 递归遍历出目录下面所有文件 
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     */  
    public static List<String> list(String pathName,FTPClient ftp){
        List<String> fileList = new ArrayList<>();
        if(pathName.startsWith("/")&&pathName.endsWith("/")){  
            String directory = pathName;  
            //更换目录到当前目录
            try {
                ftp.changeWorkingDirectory(directory);
                FTPFile[] files = ftp.listFiles();
                for(FTPFile file:files){
                    if(file.isFile()){
                        fileList.add(file.getName());
                    }else if(file.isDirectory()){
                        list(directory+file.getName()+"/",ftp);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return fileList;
    }  
      
    /** 
     * 递归遍历目录下面指定的文件名 
     * @param pathName 需要遍历的目录，必须以"/"开始和结束 
     * @param ext 文件的扩展名 
     * @throws IOException  
     */  
    public static List<String> list(String pathName,String ext,FTPClient ftp) {
        List<String> fileList = new ArrayList<>();
        if(pathName.startsWith("/")&&pathName.endsWith("/")){
            String directory = pathName;
            //更换目录到当前目录  
            try {
                ftp.changeWorkingDirectory(directory);
                FTPFile[] files = ftp.listFiles();
                for(FTPFile file:files){
                    if(file.isFile()){
                        if(file.getName().endsWith(ext)){
                            fileList.add(file.getName());
                        }
                    }else if(file.isDirectory()){
                        list(directory+file.getName()+"/",ext,ftp);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileList;
    }

    /**
     * 下载ftp指定单个文件
     * @param ftp   ftp客户端
     * @param ftpPath   文件路径
     * @param fileName   文件名
     * @param localPath   本地目标文件路径
     */
    public static void downLoadFile(FTPClient ftp, String ftpPath, String fileName, String localPath){
        try {
            ftp.changeWorkingDirectory(ftpPath);
            File localFile = new File(localPath + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFile));
            ftp.retrieveFile(fileName, bos);
            bos.close();
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
    }

    /**
     * 下载ftp目录下的所有文件
     * @param ftp
     * @param ftpPath
     * @param localPath
     */
    public static void downLoadAllFile(FTPClient ftp, String ftpPath, String localPath){

        try {
            ftp.changeWorkingDirectory(ftpPath);
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                //缺少文件筛选过程
                //TD-LTE_MRO_HUAWEI_188002145143-188002145133_20170925201500_001
                //TD-LTE_MRS_ZTE_OMC1_20180523103000


                //if (ff.getName().equals(fileName)) {
                String fileName = localPath + ff.getName();

                File localFile = new File(fileName);
                if(!localFile.exists())
                {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFile));
                    ftp.retrieveFile(ff.getName(), bos);

                }else{
                    System.out.println("文件存在");
                }
                //}
            }
            ftp.logout();
            System.out.println("文件下载完成!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
    }

}  