package com.liujiang.nettylearn.nettyProtoDemo;

import com.liujiang.nettylearn.protobuf.Testprotobuf;
import com.liujiang.nettylearn.protobuf.proto.UserProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("客户端发送的消息："+msg);
        //测试Userproto
        Testprotobuf t = new Testprotobuf();
        UserProto.User user = t.decode(t.encode());
       /* UserProto.User.Builder builder=UserProto.User.newBuilder();
        builder.setName("我是服务器");
        */
        ctx.writeAndFlush(user);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
