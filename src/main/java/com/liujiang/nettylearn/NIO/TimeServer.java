package com.liujiang.nettylearn.NIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {
    private static int PORT = 8081;
    public static void main(String[] args) {
      /*  int port = 8080;

            if (args != null && args.length > 0) {
                try {
                    port = Integer.valueOf(args[0]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }*/



        // 负责绑定IP地址，启动监听接口
        ServerSocket server=null;
            try {

                server = new ServerSocket(PORT);
                System.out.println("服务器已经启动：The time server is start in port :"+PORT);
                // 负责发起连接操作
                Socket socket = null;
                while (true) {
                    socket = server.accept();
                    new Thread(new TimeServerHandler(socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}
