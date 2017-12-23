package com.liujiang.nettylearn.NIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            String currentTime = null;
            String body = null;
            while (true) {
                body = bufferedReader.readLine();
                if (body == null) break;
                System.out.println("客户端发送的消息  The time server receive order :" + body);
                currentTime = "" + System.currentTimeMillis();
                printWriter.println("服务端响应客户端请求:亲爱的，我已经收到了你的当前时间："+currentTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭输入流
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 关闭输出流
            if (printWriter != null) {
                try {
                    printWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 关闭 网络套接字
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.socket = null;
        }
    }
}
