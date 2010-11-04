package com.protocol7.offheap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache {

    private static final int DEFAULT_ALLOCATION_SIZE = 10000;
    
    private Map<String, MemoryRange> map = new HashMap<String, MemoryRange>();
    private List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
    private ByteBuffer writeBuffer;

    private ByteBuffer allocate(int sizeHint) {
        return ByteBuffer.allocateDirect(Math.max(DEFAULT_ALLOCATION_SIZE, sizeHint));
    }
    
    private ByteBuffer getWriteBuffer(int lengthToWrite) {
        if(writeBuffer == null) {
            writeBuffer = allocate(lengthToWrite);
        }
        if(writeBuffer.remaining() < lengthToWrite) {
            writeBuffer.flip();
            buffers.add(writeBuffer);
            writeBuffer = allocate(lengthToWrite);
        }
        
        return writeBuffer;
    }

    private ByteBuffer getReadBuffer(int index) {
        if(index == buffers.size()) {
            writeBuffer.flip();
            buffers.add(writeBuffer);
            writeBuffer = null;
        }
        
        ByteBuffer buffer = buffers.get(index);
        
        return buffer;
    }

        
    public void put(String key, Serializable value) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(boas);
        
        oos.writeObject(value);
        
        byte[] bytes = boas.toByteArray(); 
        int length = bytes.length;
        
        ByteBuffer buffer = getWriteBuffer(length);
        int pos = buffer.position();

        buffer.put(bytes);
        
        map.put(key, new MemoryRange(buffers.size(), pos, length));
    }

    public Serializable get(String key) throws IOException, ClassNotFoundException {
        MemoryRange range = map.get(key);
        
        byte[] bytes2 = new byte[range.getLength()];
        
        ByteBuffer buffer = getReadBuffer(range.getBuffer());
        
        buffer.position(range.getStart());
        buffer.get(bytes2, 0, range.getLength());
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes2);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Serializable) ois.readObject();
    }
}
