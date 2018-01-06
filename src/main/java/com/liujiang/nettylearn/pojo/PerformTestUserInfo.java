package com.liujiang.nettylearn.pojo;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 *测试 jdk序列化 速度 性能  与二进制编码 的比较
 */
public class PerformTestUserInfo {
    public static void main(String[] args) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.buildUserId(100).buildUserName("Welcome to Netty");
        int loop = 1000000;
        ByteArrayOutputStream bos =null;
//        BufferedOutputStream bfos = null;
        ObjectOutputStream os = null;
        long startTime = System.currentTimeMillis();
        for (int i=0;i<loop;i++) {
            bos = new ByteArrayOutputStream();
//            bfos = new BufferedOutputStream(bos);
//            os = new ObjectOutputStream(bfos);
            os = new ObjectOutputStream(bos);
            os.writeObject(userInfo);
            os.flush();
            os.close();
            byte[] b = bos.toByteArray();
//            bfos.close();
            bos.close();
        }
        long endTime=System.currentTimeMillis();
        System.out.println("The jdk serializable cost time is : " + (endTime - startTime) + "ms");
        System.out.println("---------------------------------------------");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        startTime = System.currentTimeMillis();
        for (int i=0;i<loop;i++) {
            byte[] b = userInfo.codeC(buffer);
        }
        endTime=System.currentTimeMillis();
        System.out.println("The byte array serializable cost time is :"+(endTime-startTime)+"ms");
    }

}
