package com.xybbz.config;

import com.sun.xml.internal.bind.v2.TODO;
import lombok.AllArgsConstructor;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FtpClientUtil {
    private FTPClient ftpClient = null;
    private String server;
    private int port;
    private String userName;
    private String userPassword;


    public static void main(String[] args) {
        FtpClientUtil f = new FtpClientUtil("49.234.105.52", 21, "fendo", "03145215Ll");
        try {
            //判断是否链接成功
            if (f.open()) {
                // 远程路径为相对路径
                // todo 2020-11-25 17:55:xx 未完成
//                f.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public FtpClientUtil(String server, int port, String userName, String userPassword) {
        this.server = server;
        this.port = port;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * 链接服务器
     *
     * @author ASUS
     */
    public boolean open() {
        if (ftpClient != null && ftpClient.isConnected()) {
            return true;
        }
        //服务器建立链接
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(this.server, this.port);
            ftpClient.login(this.userName, this.userPassword);
            // 检测连接是否成功
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.clone();
                System.exit(1);
            }
            // 设置上传模式binally or ascii
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // 关闭
            this.close();
            return false;
        }


    }

    /**
     * 关闭链接
     */
    public void close() {
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从ftp上下载文件 并返回
     *
     * @param ftpDirectoryAndFileName   ftp服务器文件所在路径和文件名
     * @param localDirectoryAndFileName 下载到本地的位置
     * @return
     */
    public long get(String ftpDirectoryAndFileName, String localDirectoryAndFileName) {
        long result = 0;
        //判断是否已经链接
        if (!ftpClient.isConnected()) {
            return 0;
        }
        //这个方法的意思就是每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据
        ftpClient.enterLocalActiveMode();
        try {
            //将路径中的斜杠统一,将转换好的可变字符串转换为String
            ftpDirectoryAndFileName = conversion(ftpDirectoryAndFileName);
            //截取从0-最后一个/的字符串获得路径
            String filePath = ftpDirectoryAndFileName.substring(0, ftpDirectoryAndFileName.lastIndexOf('/'));
            //获得文件名
            String fileName = ftpDirectoryAndFileName.substring(ftpDirectoryAndFileName.lastIndexOf('/') + 1);
            this.changeDir(filePath);
            //下载
            ftpClient.retrieveFile(new String(fileName.getBytes(), StandardCharsets.ISO_8859_1),
                    new FileOutputStream(localDirectoryAndFileName));
            // check result  检查结果
            System.out.print(ftpClient.getReplyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 设置文件
     *
     * @param ftpPath
     * @return
     */
    private boolean changeDir(String ftpPath) {
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            //处理'\\'的问题
            ftpPath = conversion(ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                if (Objects.equals(ftpClient.printWorkingDirectory(), ftpPath)) {
                    ftpClient.makeDirectory(new String(ftpPath.getBytes(), StandardCharsets.ISO_8859_1));
                } else {
                    //只有一层目录
                    ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), StandardCharsets.ISO_8859_1));
                }
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                for (String path : paths) {
                    ftpClient.makeDirectory(new String(path.getBytes(), StandardCharsets.ISO_8859_1));
                    ftpClient.changeWorkingDirectory(new String(path.getBytes(), StandardCharsets.ISO_8859_1));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 处理 \\ 的问题
     */
    private String conversion(String filePath) {
        char[] chars = filePath.toCharArray();
        StringBuffer buStr = new StringBuffer(256);
        for (char aChar : chars) {
            if ('\\' == aChar) {
                buStr.append('/');
            } else {
                buStr.append(aChar);
            }
        }
        return buStr.toString();
    }
}

