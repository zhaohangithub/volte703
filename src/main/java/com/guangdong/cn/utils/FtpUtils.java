package com.guangdong.cn.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPCommand;
import org.apache.commons.net.ftp.FTPFile;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.ivy.core.report.DownloadStatus;
import org.apache.log4j.Logger;

/** 
 * 操作FTP服务器上指定目录下面满足条件的所有文件
 */  
public class FtpUtils {
    private static Logger logger = Logger.getLogger(FtpUtils.class);
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
    public static void downLoadFile(FTPClient ftp, String ftpPath, String fileName, String localPath){
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断点续传
     * @param ftp       ftp客户端
     * @param ftpPath   文件路径
     * @param fileName  文件名
     * @param localPath 本地目标文件路径
     */
    public static DownloadStatus downLoadFile(FTPClient ftp, String ftpPath, String fileName, String localPath, int fileIndex) throws IOException {
        //设置linux环境
        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        ftp.configure(conf);

        //设置被动推模式
        ftp.setRemoteVerificationEnabled(false);
        ftp.enterLocalPassiveMode();
        //设置客户端缓冲区
        ftp.setBufferSize(1024*1024);
        //设置二进制文件传输格式
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        DownloadStatus result;
        //更改工作目录
        ftp.changeWorkingDirectory(ftpPath);
        File localFile = new File(localPath + fileName);
        FTPFile[] files = ftp.listFiles(new String(ftpPath.getBytes("UTF-8"),"ISO-8859-1"));
        long lRemoteSize = files[fileIndex].getSize();
        if (localFile.exists()) {//判断本地有没有该文件
            long localSize = localFile.length();
            if (localSize >= lRemoteSize) {
                System.out.println("本地文件已存在远程文件，下载中止");
                logger.info("本地文件已存在远程文件，下载中止");
                return DownloadStatus.Local_Bigger_Remote;
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFile, true));
            ftp.setRestartOffset(localSize);
            InputStream in = ftp.retrieveFileStream(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            //ftp.retrieveFile(fileName, bos);
            try {
                byte[] bytes = new byte[1024*1024*8];
                //总的进度
                long step = lRemoteSize / 100;
                long process = localSize / step;
                int c;
                while ((c = in.read(bytes)) != -1) {
                    bos.write(bytes, 0, c);
                    bos.flush();
                    localSize += c;
                    long nowProcess = localSize / step;
                    if (nowProcess > process) {
                        process = nowProcess;
                        if (process % 10 == 0) {
                            System.out.println("下载进度：" + process);
                            logger.info("下载进度：" + process);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null)
                    in.close();
                if (bos != null)
                    bos.close();
            }
            //确认是否全部下载完毕
            boolean isDo = ftp.completePendingCommand();
            if (isDo) {
                result = DownloadStatus.Download_From_Break_Success;
                logger.info("断点下载文件成功");
            } else {
                result = DownloadStatus.Download_From_Break_Failed;
                logger.info("断点下载文件失败");
            }
        } else {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFile));
            InputStream in = ftp.retrieveFileStream(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            try {
                byte[] bytes = new byte[1024*1024*8];
                long step = lRemoteSize / 100;
                long process = 0;
                long localSize = 0;
                int c;
                while ((c = in.read(bytes)) != -1) {
                    bos.write(bytes, 0, c);
                    bos.flush();
                    localSize += c;
                    long nowProcess = localSize / step;
                    if (nowProcess > process) {
                        process = nowProcess;
                        if (process % 10 == 0) {
                            System.out.println("下载进度：" + process);
                            logger.info("下载进度：" + process);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null)
                    in.close();
                if (bos != null)
                    bos.close();
            }
            boolean upNewStatus = ftp.completePendingCommand();
            if (upNewStatus) {
                result = DownloadStatus.Download_New_Success;
                logger.info("全新下载文件成功");
            } else {
                result = DownloadStatus.Download_New_Failed;
                logger.info("全新下载文件失败");
            }
        }
        return result;
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

    public enum DownloadStatus {
        Local_Bigger_Remote, // 本地文件已存在远程文件
        Download_From_Break_Success, // 断点下载文件成功
        Download_From_Break_Failed, // 断点下载文件失败
        Download_New_Success, // 全新下载文件成功
        Download_New_Failed,// 全新下载文件失败
    }
}