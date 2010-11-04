/**
 * 
 */
package com.protocol7.offheap;

public class MemoryRange {
    private int buffer;
    private int start;
    private int length;
    public MemoryRange(int buffer, int start, int length) {
        this.buffer = buffer;
        this.start = start;
        this.length = length;
    }

    public int getBuffer() {
        return buffer;
    }

    public int getStart() {
        return start;
    }
    public int getLength() {
        return length;
    }
}