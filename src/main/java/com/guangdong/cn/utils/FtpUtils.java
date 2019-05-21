package com.guangdong.cn.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

import org.apache.commons.pool2.impl.GenericObjectPool;

/** 
 * 操作FTP服务器上指定目录下面满足条件的所有文件
 */  
public class FtpUtils {

    /** 
     * 递归遍历出目录下面所有文件 
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     */  
    public static List<String> list(String pathName,FTPClient ftp){
        //设置linux环境
//        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
//        ftp.configure(conf);
        ftp.setRemoteVerificationEnabled(false);//远程授权
        List<String> fileList = new ArrayList<>();
        if(pathName.startsWith("/")&&pathName.endsWith("/")){
            try {
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);//二进制
                ftp.changeWorkingDirectory(pathName);//更换目录到当前目录
                FTPFile[] files = ftp.listFiles();
                for(FTPFile file:files){
                    if(file.isFile()){//文件
                        fileList.add(file.getName());//加入list
                    }else if(file.isDirectory()){//文件夹
                        list(pathName+file.getName()+"/",ftp);//递归
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
    public static boolean downLoadFile(FTPClient ftp, String ftpPath, String fileName, String localPath){
        //设置linux环境
//        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
//        ftp.configure(conf);
        try {
            if (ftp.isConnected()){
                ftp.setRemoteVerificationEnabled(false);//远程检查授权
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);//二进制
                ftp.changeWorkingDirectory(ftpPath);//更改工作目录
                File localFile = new File(localPath + fileName);
                if (!localFile.exists()){//判断本地有没有该文件
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFile));
                    ftp.retrieveFile(fileName, bos);//文件下载
                    bos.close();
                }
            }else {
                System.err.println("ftp连接已断开!");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void upLoadFile(FTPClient ftp, String ftpPath, String fileName){
        System.out.println("文件上传");
        //设置linux环境
//        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
//        ftp.configure(conf);
        try {
            if (ftp.isConnected()){
                File file = new File(fileName);
                String name = file.getName();
                ftp.setRemoteVerificationEnabled(false);//远程检查授权
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);//二进制
                ftp.changeWorkingDirectory(ftpPath);//更改工作目录
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                ftp.appendFile(name,bis);
                bis.close();
            }else {
                System.err.println("ftp连接已断开!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}