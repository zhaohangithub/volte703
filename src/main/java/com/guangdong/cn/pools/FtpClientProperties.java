package com.guangdong.cn.pools;


import com.guangdong.cn.utils.GlobalConfUtils;
import org.jruby.RubyBoolean;

public class FtpClientProperties {
    // ftp地址
    private String FtpUrl = GlobalConfUtils.FtpUrl;
    // 端口号
    private Integer FtpPort = GlobalConfUtils.FtpPort;
    // 登录用户
    private String FtpUsername = GlobalConfUtils.FtpUsername;
    // 登录密码
    private String FtpPassword = GlobalConfUtils.FtpPassword;
    // 被动模式
    private boolean PassiveMode = true;
    // 编码
    private String Encoding = "UTF-8";
    // 连接超时时间(秒)
    private Integer ConnectTimeout = 60000;
    // 缓冲大小
    private Integer bufferSize = 1024*8;
    // 传输文件类型
    private Integer transferFileType;



    public String getFtpUrl() {
        return FtpUrl;
    }

    public void setFtpUrl(String ftpUrl) {
        FtpUrl = ftpUrl;
    }

    public Integer getFtpPort() {
        return FtpPort;
    }

    public void setFtpPort(Integer ftpPort) {
        FtpPort = ftpPort;
    }

    public String getFtpUsername() {
        return FtpUsername;
    }

    public void setFtpUsername(String ftpUsername) {
        FtpUsername = ftpUsername;
    }

    public String getFtpPassword() {
        return FtpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        FtpPassword = ftpPassword;
    }

    public boolean isPassiveMode() {
        return PassiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        PassiveMode = passiveMode;
    }

    public String getEncoding() {
        return Encoding;
    }

    public void setEncoding(String encoding) {
        Encoding = encoding;
    }

    public Integer getConnectTimeout() {
        return ConnectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        ConnectTimeout = connectTimeout;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Integer getTransferFileType() {
        return transferFileType;
    }

    public void setTransferFileType(Integer transferFileType) {
        this.transferFileType = transferFileType;
    }
}