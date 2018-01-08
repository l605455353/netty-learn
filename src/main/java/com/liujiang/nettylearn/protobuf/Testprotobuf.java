package com.liujiang.nettylearn.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.liujiang.nettylearn.protobuf.proto.UserProto;

import java.util.ArrayList;
import java.util.List;

public class Testprotobuf {

    public byte[] encode() {
        // 创建address对象（是user的内部类）
        UserProto.User.Address.Builder bu = UserProto.User.Address.newBuilder();
        bu.setProvince("shanxi");
        bu.setPostcode(710055);

        UserProto.User.Address address=bu.build();
        // 创建user对象
        UserProto.User.Builder builder = UserProto.User.newBuilder();
        builder.setName("Taylor");
        builder.setPassword(123456);
        builder.setPhone(82927222);
        builder.setAddress(address);
        builder.setSex("Woman");

        List<String> list = new ArrayList<String>();
        list.add("basketball");
        list.add("badminton");
        list.add("soccer");
        builder.addAllHobby(list);
        byte[] bytes = builder.build().toByteArray();
        return bytes;
    }

    public static UserProto.User decode(byte[] body) throws InvalidProtocolBufferException {
        return UserProto.User.parseFrom(body);
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        Testprotobuf t = new Testprotobuf();
        UserProto.User user = t.decode(t.encode());
        System.out.println("解码后信息：" + user);
        System.out.println("状态："+user.getUser());
    }

}
