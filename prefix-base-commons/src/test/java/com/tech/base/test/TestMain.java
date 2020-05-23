package com.tech.base.test;

public class TestMain {

    public static void main(String[] args) {
        int hash = hash("abfsdfsdfsfsfsdfsdfsf");
       System.out.println(hash);

    }

    static final int hash(Object key) {
        int h;
        int a = (h = key.hashCode());
        int b = (h >>> 16);
        int c = a ^ b;
        return (key == null) ? 0 : c;
    }
}
