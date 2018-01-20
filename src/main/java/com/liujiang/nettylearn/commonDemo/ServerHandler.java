package com.liujiang.nettylearn.commonDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 用于网络的读写操作
        //do something msg
        ByteBuf buf = (ByteBuf)msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String request = new String(data, "utf-8");
        System.out.println("Server: " + request);
        //写给客户端
       String response = "我是服务端8379";
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
        //.addListener(ChannelFutureListener.CLOSE);


    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();   // 它的作用是把消息发送队列中的消息写入SocketChannel中发送给对方
        // 为了防止频繁的唤醒Selector进行消息发送，Netty的write方法，并不直接将消息写入SocketChannel中
        // 调用write方法只是把待发送的消息发到缓冲区中，再调用flush，将发送缓冲区中的消息 全部写到SocketChannel中。
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
