package com.liujiang.nettylearn;


import org.msgpack.MessagePack;
import org.msgpack.type.Value;


import javax.xml.transform.Templates;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test2 {
    public static void main(String[] args) throws IOException {
        List<String> src = new ArrayList<>();
        src.add("liujiang");
        src.add("lives");
        src.add("life");
        MessagePack messagePack = new MessagePack();
        byte[] raw = messagePack.write(src);
        // http://blog.csdn.net/woshiliufeng/article/details/50148677


    }
}
