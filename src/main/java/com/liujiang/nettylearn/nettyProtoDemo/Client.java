package com.liujiang.nettylearn.nettyProtoDemo;

import com.liujiang.nettylearn.protobuf.proto.UserProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                        // 用于处理半包消息的解码类
                        socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        // 需要解码的目标类
                       socketChannel.pipeline().addLast(new ProtobufDecoder(UserProto.User.getDefaultInstance()));
                        // 对protobuf协议的消息头上加上一个长度为32的整形字段，用于标志这个消息的长度的类
                        socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        // 编码类
                        socketChannel.pipeline().addLast(new ProtobufEncoder());
                        socketChannel.pipeline().addLast(new ClientHandler());

                    }
                });
        // 发起异步连接操作
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8379).sync();
        future.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端".getBytes()));

        future.channel().closeFuture().sync();
        workerGroup.shutdownGracefully();
    }
}
