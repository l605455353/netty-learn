package com.liujiang.nettylearn.nettyProtoDemo;


import com.liujiang.nettylearn.protobuf.Testprotobuf;
import com.liujiang.nettylearn.protobuf.proto.UserProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            System.out.println("服务端发送的信息："+msg);
            Testprotobuf t = new Testprotobuf();
            UserProto.User user2 = t.decode(t.encode());
          /*UserProto.User.Builder builder=UserProto.User.newBuilder();
            builder.setName("我是客服端");*/

            ctx.writeAndFlush(user2);
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
