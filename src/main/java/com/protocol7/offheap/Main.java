package com.protocol7.offheap;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        String test = "Hello world";

        Cache cache = new Cache();
        
        for(int i = 0; i<2000; i++) {
            cache.put("foo", test);
        }
        
        System.out.println(cache.get("foo"));

        cache.put("bar", test);
        System.out.println(cache.get("bar"));
    }
}
