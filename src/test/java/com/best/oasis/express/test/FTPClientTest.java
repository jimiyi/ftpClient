package com.best.oasis.express.test;

import com.best.oasis.express.ftp.FTPClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * FTPClient 的测试类
 *
 * @author yiwei
 * @create 2017/8/25
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-config.xml"})
public class FTPClientTest {

//    @Test
//    public void upload1() throws Exception {
//        byte[] content = fileToByte("D:\\project\\ftpService\\test\\cce66928-862e-4683-907b-db4c5b14fe2b0.csv");
//        FTPClient.upload("cce66928-862e-4683-907b-db4c5b14fe2b0.csv",content,"yiwei");
//    }
//
//    @Test
//    public void upload2() throws Exception {
//        File file = new File("D:\\project\\ftpService\\test\\cce66928-862e-4683-907b-db4c5b14fe2b1.csv");
//        FTPClient.upload(file,"yiwei");
//    }
//
//    @Test
//    public void download() throws Exception {
//        String TEMPLATE = "print-template.csv";
//        FTPClient.download(TEMPLATE, "/print/CN_EBILL_1/", "/print/CN_EBILL_1");
//    }
//
//    @Test
//    public void append() throws Exception {
//        FTPClient.append("yiwei/测试.txt","海阔天空".getBytes());
//    }
//
//    @Test
//    public void delete() throws Exception {
//        FTPClient.delete("yiwei/测试.txt");
//    }
//
//    private byte[] fileToByte(String fileName) throws Exception{
//        FileInputStream fis = new FileInputStream(new File(fileName));
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        byte[] b = new byte[185*1024];
//        int len = -1;
//        while((len = fis.read(b)) != -1) {
//            bos.write(b, 0, len);
//        }
//        return bos.toByteArray();
//    }
//
    @Test
    public void getFileNameAndPath(){
        String file = "/home/yiwei/yw.txt";
        String[] paths = file.split("/");
        String fileName = paths[paths.length - 1];
        String storePath = file.substring(0,file.indexOf(paths[paths.length - 1]));
        System.out.println(fileName);
        System.out.println(storePath);
    }
}
