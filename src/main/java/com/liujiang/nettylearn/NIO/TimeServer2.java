package com.liujiang.nettylearn.NIO;

public class TimeServer2 {
    private static int PORT = 8082;
    public static void main(String[] args){
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(PORT);
        new Thread(timeServer).start();
    }
}
