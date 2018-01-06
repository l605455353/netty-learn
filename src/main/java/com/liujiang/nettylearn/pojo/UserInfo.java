package com.liujiang.nettylearn.pojo;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class UserInfo implements Serializable{

    private static final long serialVersionUID = 4946872859589776624L;

    private String userName;
    private int UserId;

    public UserInfo buildUserName(String userName) {
        this.userName=userName;
        return this;
    }

    public UserInfo buildUserId(int userId) {
        this.UserId=userId;
        return this;
    }

    public byte[] codec() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.UserId);
        // 用于读取buffer中的数据
        buffer.flip();
        value=null;
        // buffer.remaining() 查看剩余可用的空间

        // result 写入buffer缓存区的user数据大小
        byte[] result = new byte[buffer.remaining()];
        return result;
    }


    public byte[] codeC(ByteBuffer buffer) {
        //　clear() --重新写入数据使用,将当前可用的第一个位置指向了buffer的首位值。
        buffer.clear();
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.UserId);
        buffer.flip();
        value=null;
        byte[] result = new byte[buffer.remaining()];
        return result;
    }

    public final String getUserName() {
        return userName;
    }

    public final void setUserName(String userName) {
        this.userName=userName;
    }

    public final int getUserId() {
        return UserId;
    }

    public final void setUserId(int userId) {
        this.UserId=userId;
    }



}
