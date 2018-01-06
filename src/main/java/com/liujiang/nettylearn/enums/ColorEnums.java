package com.liujiang.nettylearn.enums;

public enum ColorEnums implements Behaviour{
    RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);

    private String name;
    private int index;

    private ColorEnums(String name, int index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public String getInfo() {
        return this.name;
    }

    @Override
    public void print() {
        System.out.println(this.index+":"+this.name);
    }
}
