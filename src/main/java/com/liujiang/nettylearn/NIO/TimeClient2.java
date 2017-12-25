package com.liujiang.nettylearn.NIO;



public class TimeClient2 {
    private static int PORT = 8082;
    private static String IP = "127.0.0.1";
    public static void main(String[] args){


        new Thread(new TimeClientHandle(IP,PORT)).start();

    }
}
