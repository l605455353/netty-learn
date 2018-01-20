package com.liujiang.nettylearn.javaIO.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

public class MultiplexerTimeServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    // 线程在每次使用volatile变量的时候，都会读取变量修改后的最的值。
    private volatile boolean stop;

    /**
     * 初始化多用复用器，绑定监听端口在构造方法中进行资源初始化，进行参数配置
     *
     * @param port
     */
    public MultiplexerTimeServer(int port) {

        // 打开复用器
        try {
            selector = Selector.open();
            //  打开服务器通道
            serverSocketChannel = ServerSocketChannel.open();
            //  非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //  绑定监听端口 backlog设置为1024
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            //  注册到复用器上 监听SelectionKey.OP_ACCEPT操作位
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port :" + port);
        } catch (IOException e) {
            e.printStackTrace();
            // 退出进程
            System.exit(1);
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
                SelectionKey key = null;
                //3 遍历keys
                while (keys.hasNext()) {
                    key = keys.next();
                    keys.remove();
                    try {
                        handleInput(key);
                    }catch (Exception e){
                        if(key!=null)
                            key.cancel();
                        if(key.channel()!=null){
                            key.channel().close();
                        }
                    }
                }

            } catch (Throwable T) {
                T.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key)throws IOException{
        if(key.isValid()){ ////如果key的状态是有效的
            if(key.isAcceptable()) accept(key);  // 处理新接入的请求消息 如果key是阻塞状态，则调用accept()方法
            if (key.isReadable()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //1 清空缓冲区中的旧数据
            byteBuffer.clear();
                //2 获取之前注册的SocketChannel通道
                SocketChannel sc = (SocketChannel) key.channel();
                ///3 将sc中的数据放入buffer中
                int count = sc.read(byteBuffer);
                // count>0，读到字节，count==0 没有读取到字节，属于正常场景，忽略；count==-1，链路已经关闭，需要关闭SocketChannel，释放资源

                if (count > 0) {
                    //读取到了数据，将buffer的position复位到0
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    //将buffer中的数据写入byte[]中
                    byteBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("服务器读取到的数据："+body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(
                            System.currentTimeMillis()).toString()
                            :"BAD ORDER";
                    doWrite(sc,currentTime);


                    doWrite(sc,"客户端返回消息:"+currentTime);
                }else if (count <0) {
                    // 对端链路关闭
                    key.cancel();
                    sc.close();
                }else ; // 读到0字节，忽略









//                read(key);
                // read data
               /* SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if(readBytes>0){
                    readBuffer.flip();
                    byte[] bytes=new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("recvive order : " + body);

                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(
                            System.currentTimeMillis()).toString()
                            :"BAD ORDER";
                    doWrite(sc,currentTime);
                }
                else if(readBytes<0){
                    // 对端链路关闭
                    key.cancel();
                    sc.close();
                }
                else ; // 读到0字节，忽略*/



            }
        }
    }


    /**
     * 将应答消息异步发送给客户端
     * @param channel
     * @param response
     * @throws IOException
     */
    private void doWrite(SocketChannel channel,String response)throws IOException{
        if(response!=null&&response.trim().length()>0){
            byte[]bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            // 将字节数组复制到缓冲区
            writeBuffer.put(bytes);
            // 对缓冲区进行flip操作
            writeBuffer.flip();
            // write方法发送出去
            channel.write(writeBuffer);
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

}
