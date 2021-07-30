/**
 * Copyright 2021 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.thierrysquirrel.hummingbird.core.facade;

import lombok.Data;

import java.nio.ByteBuffer;

/**
 * Classname: ByteBufferFacade
 * Description:
 * Date: 2021/7/29 20:04
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
@Data
public class ByteBufferFacade {
    private ByteBuffer byteBuffer;
    private int makePosition = -1;

    public boolean readComplete() {
        int position = byteBuffer.position ();
        int limit = byteBuffer.limit ();
        return position < limit;
    }

    public int length() {
        int position = byteBuffer.position ();
        int limit = byteBuffer.limit ();
        return limit - position;
    }

    public void flip() {
        byteBuffer.flip ();

    }

    public void reclaim() {
        int length = length ();
        if (length <= 0) {
            byteBuffer.clear ();
            return;
        }
        byte[] cache = new byte[length];
        byteBuffer.get (cache);
        byteBuffer.clear ();
        byteBuffer.put (cache);
    }

    public void clear() {
        byteBuffer.clear ();
        clearMake ();
    }

    public void make() {
        setMakePosition (byteBuffer.position ());
    }

    public void reset() {
        byteBuffer.position (getMakePosition ());
    }

    public void clearMake() {
        setMakePosition (-1);
    }

    public void putBytes(byte[] value) {
        byteBuffer.put (value);
    }

    public void putByte(byte value) {
        byteBuffer.put (value);
    }

    public void putShort(short value) {
        byteBuffer.putShort (value);
    }

    public void putInt(int value) {
        byteBuffer.putInt (value);
    }

    public void putLong(long value) {
        byteBuffer.putLong (value);
    }

    public void putFloat(float value) {
        byteBuffer.putFloat (value);
    }

    public void putDouble(double value) {
        byteBuffer.putDouble (value);
    }

    public void putChar(char value) {
        byteBuffer.putChar (value);
    }

    public byte getByte() {
        return byteBuffer.get ();
    }

    public void getBytes(byte[] value) {
        byteBuffer.get (value);
    }

    public short getShort() {
        return byteBuffer.getShort ();
    }

    public int getInt() {
        return byteBuffer.getInt ();
    }

    public long getLong() {
        return byteBuffer.getLong ();
    }

    public float getFloat() {
        return byteBuffer.getFloat ();
    }

    public double getDouble() {
        return byteBuffer.getDouble ();
    }

    public char getChar() {
        return byteBuffer.getChar ();
    }
}
