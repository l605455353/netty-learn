package com.liujiang.nettylearn.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 多路复用类
 */
public class MultiplexerTimeServer implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;
    // 开辟一个1MB的缓冲区
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);


    /**
     * 初始化多用复用器，绑定监听端口
     *在构造方法中进行资源初始化，进行参数配置
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            // 打开复用器
            selector = Selector.open();
            //  打开服务器通道
            serverSocketChannel = ServerSocketChannel.open();
            //  非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //  绑定监听端口 backlog设置为1024
            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
            //  注册到复用器上 监听SelectionKey.OP_ACCEPT操作位
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port :" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                //1 必须让多路复用选择器开始监听
//            selector.select();    //表示当有处于就绪状态的Channel时，返回SelectionKey集合
                // 设置selector的休眠时间为1秒，无论是否有读写等事件发生，其每隔一秒被唤醒一次
                selector.select(1000);
                //2 返回所有已经注册到多路复用选择器上的通道的SelectionKey
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                //3 遍历keys
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    //如果key的状态是有效的
                    if (key.isValid()) {
                        //如果key是阻塞状态，则调用accept()方法
                        if (key.isAcceptable()) accept(key);
                        if (key.isReadable())read(key);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 关闭多路复用器
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据SelectionKey的操作位进行判断可获知网络事件的类型
     * 通过ServerSocketChannel的accept接受客户端的连接请求并创建SocketChannel实例（相当于完成了TCP的三次握手）
     * @param key
     */
    private void accept(SelectionKey key) {

        try {
            //1 获取服务器通道
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            //2 执行阻塞方法
            SocketChannel sc = ssc.accept();
            //3 设置阻塞模式为非阻塞
            sc.configureBlocking(false);
            //4 注册到多路复用选择器上，并设置读取标识
            sc.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取客户端的请求消息
     * @param key
     */
    private void read(SelectionKey key) {
        try {
            //1 清空缓冲区中的旧数据
            byteBuffer.clear();
            //2 获取之前注册的SocketChannel通道
            SocketChannel sc = (SocketChannel) key.channel();
            ///3 将sc中的数据放入buffer中
            int count = sc.read(byteBuffer);
            // count>0，读到字节，count==0 没有读取到字节，属于正常场景，忽略；count==-1，链路已经关闭，需要关闭SocketChannel，释放资源
            if (count == -1) {
                // 对端链路关闭
                key.cancel();
                sc.close();
                return;
            }
            //读取到了数据，将buffer的position复位到0
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            //将buffer中的数据写入byte[]中
            byteBuffer.get(bytes);
            String body = new String(bytes, "UTF-8");
            System.out.println("服务器读取到的数据："+body);

            doWrite(sc,"我是客户端，我已经返回给你消息啦");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将应答消息异步发送给客户端
     * @param channel
     * @param response
     */
    private void doWrite(SocketChannel channel, String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            // 将字节数组复制到缓冲区
            byteBuffer.put(bytes);
            // 对缓冲区进行flip操作
            byteBuffer.flip();
            // write方法发送出去
            channel.write(byteBuffer);
        }
    }
}
