package com.liujiang.nettylearn.IO;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
    private static int PORT = 8081;
    public static void main(String[] args) {

        // 负责绑定IP地址，启动监听接口
        ServerSocket server=null;
            try {

                server = new ServerSocket(PORT);
                System.out.println("服务器已经启动：The time server is start in port :"+PORT);
                //采用线程池的方式
               HandlerExecutorPool pool = new HandlerExecutorPool(50, 1000);
                // 负责发起连接操作
                Socket socket = null;
                while (true) {
                    socket = server.accept();
                    new Thread(new TimeServerHandler(socket)).start();

                  /*  // 线程池启动
                    pool.execute(new TimeServerHandler(socket));*/
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}
