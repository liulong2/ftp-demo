package com.xybbz.config;

import ch.qos.logback.core.property.FileExistsPropertyDefiner;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author ASUS
 */
public class Ftp {

    private static FTPClient ftpClient = new FTPClient();
    private static String encoding = System.getProperty("file.encoding");

    /**
     * Description: 向FTP服务器上传文件
     * @param url
     * @param port
     * @param userName
     * @param password
     * @param path
     * @param filename
     * @param input
     * @return
     */

    public static boolean uploadFile(String url, int port, String userName, String password, String path,
                                        String filename, InputStream input) {
        boolean result = false;
        try {
            int reply;
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
//            ftpClient.connect(url);
            ftpClient.connect(url,port);
            // 登录
            ftpClient.login(userName, password);
            ftpClient.setControlEncoding(encoding);
            // 检验是否连接成功
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("连接失败");
                ftpClient.disconnect();
                return result;
            }

            // 转移工作目录至指定目录下
            boolean change = ftpClient.changeWorkingDirectory(path);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (change) {
                result = ftpClient.storeFile(new String(filename.getBytes(encoding), StandardCharsets.ISO_8859_1), input);
                if (result) {
                    System.out.println("上传成功!");
                }
            }
            input.close();
            ftpClient.logout();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        testUpLoadFromDisk();
    }
    public static void testUpLoadFromDisk() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("C:/Users/ASUS/Desktop/git安装.txt"));
            boolean flag = uploadFile("49.234.105.52", 21, "root", "03145215Ll", "/",
                    "哈哈.txt", fileInputStream);
            System.out.println(flag);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}

