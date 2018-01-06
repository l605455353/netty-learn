package com.liujiang.nettylearn.pojo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * JDK序列化编码后的二进制大小  与 二进制编码大小对比
 *
 */
public class TestUserInfo {
    public static void main(String[] args) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.buildUserId(100).buildUserName("liujiang");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(userInfo);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        System.out.println("The jdk array serializable length is : " + b.length);
        bos.close();
        System.out.println("-------------------------------");
        System.out.println("The byte array serializable length is : "+ userInfo.codec().length);
    }
}
