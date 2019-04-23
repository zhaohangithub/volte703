package com.guangdong.cn.pools;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import sun.rmi.runtime.Log;

import java.io.IOException;

public class FtpClientFactory extends BasePooledObjectFactory<FTPClient> {

    private FtpClientProperties config;

    public FtpClientFactory(FtpClientProperties config) {
        this.config = config;
    }


    /**
     * 创建FtpClient对象
     */
    @Override
    public FTPClient create() {

        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(config.getEncoding());
        ftpClient.setConnectTimeout(config.getConnectTimeout());
        try {

            ftpClient.connect(config.getFtpUrl(), config.getFtpPort());
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                System.out.println("FTPServer refused connection,replyCode:{}");
                return null;
            }

            if (!ftpClient.login(config.getFtpUsername(), config.getFtpPassword())) {
                System.out.println("ftpClient login failed... username is {}; password: {}"+ config.getFtpUsername()+config.getFtpPassword());
            }

            ftpClient.setBufferSize(config.getBufferSize());
            if (config.isPassiveMode()) {
                ftpClient.enterLocalPassiveMode();
            }

        } catch (IOException e) {
            System.out.println("create ftp connection failed...");
            e.printStackTrace();
        }
        return ftpClient;
    }

    /**
     * 用PooledObject封装对象放入池中
     */
    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    /**
     * 销毁FtpClient对象
     */
    @Override
    public void destroyObject(PooledObject<FTPClient> ftpPooled) {
        if (ftpPooled == null) {
            return;
        }

        FTPClient ftpClient = ftpPooled.getObject();

        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    /**
     * 验证FtpClient对象
     */
    @Override
    public boolean validateObject(PooledObject<FTPClient> ftpPooled) {
        try {
            FTPClient ftpClient = ftpPooled.getObject();
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}