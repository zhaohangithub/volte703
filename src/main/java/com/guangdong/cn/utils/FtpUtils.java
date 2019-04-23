package com.guangdong.cn.utils;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * 单连接,单文件操作
 */
public class FtpUtils {

    /**
     * 获取ftp连接
     * @param url
     * @param port
     * @param username
     * @param password
     * @return
     */
    public static FtpClient connectFtp(String url, int port, String username, String password){
        FtpClient ftpClient = null;
        try{
            //连接地址
            InetSocketAddress Address = new InetSocketAddress(url, port);
            //连接
            ftpClient = FtpClient.create(Address);
            //登录
            ftpClient.login(username,password.toCharArray());
            ftpClient.setBinaryType();

        }catch (Exception e){
            e.printStackTrace();
        }
        return ftpClient;
    }

    /**
     * 上传文件到ftp服务器
     * @param localFile
     * @param ftpFile
     * @param ftp
     */
    public static void upload(String localFile, String ftpFile, FtpClient ftp){

    }

    /**
     * 从ftp服务器下载文件
     * @param localFile
     * @param ftpFile
     * @param ftp
     */
    public static void download(String localFile, String ftpFile, FtpClient ftp){
        InputStream in = null;
        OutputStream out = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos =null;
        try {
            //输入流
            in = ftp.getFileStream(ftpFile);
            bis = new BufferedInputStream(in);
            //输出流
            out = new FileOutputStream(localFile);
            bos = new BufferedOutputStream(out);
            byte[] bys = new byte[1024];
            int len =0;
            while((len = bis.read(bys))!=-1){
                bos.write(bys, 0, len);
                bos.flush();
            }
        } catch (FtpProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭连接
                ftp.close();
                //关闭流
                if (bos != null) {
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
