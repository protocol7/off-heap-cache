package com.protocol7.offheap;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class CacheTest {

    private static final String TEST_STRING = "Hello world";
    private static final String TEST_STRING2 = "Hello world2";

    @Test
    public void test() throws IOException {

        Cache cache = new Cache();
        cache.put("foo", TEST_STRING);
        
        Assert.assertEquals(TEST_STRING, cache.get("foo"));

        cache.put("bar", TEST_STRING2);
        Assert.assertEquals(TEST_STRING2, cache.get("bar"));
    }
}
