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

import com.github.thierrysquirrel.hummingbird.core.facade.constant.ByteBufferFacadeConstant;
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
        return length (byteBuffer);
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
        reallocateCapacity (byteBuffer.capacity ());
    }

    public boolean isExpansion() {
        int capacity = byteBuffer.capacity ();
        double expansionThreshold = capacity * ByteBufferFacadeConstant.EXPANSION_THRESHOLD;
        int position = byteBuffer.position ();
        if (position < expansionThreshold) {
            return Boolean.FALSE;
        }
        int newSize = capacity * ByteBufferFacadeConstant.EXPANSION_SIZE;
        if (newSize > ByteBufferFacadeConstant.MAX_SIZE) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void tryExpansion(boolean expansion) {
        if (expansion) {
            expansion ();
        } else {
            reclaim ();
        }
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

    private void automaticExpansion() {
        if (isExpansion ()) {
            byteBuffer.flip ();
            expansion ();
        }
    }

    private void cyclicExpansion(int valueSize) {
        while (valueSize > length ()) {
            byteBuffer.flip ();
            expansion ();
        }
    }

    private void expansion() {
        int capacity = byteBuffer.capacity ();
        int newSize = capacity * ByteBufferFacadeConstant.EXPANSION_SIZE;
        reallocateCapacity (newSize);
    }

    private void reallocateCapacity(int capacity) {
        ByteBuffer newBytebuffer = ByteBuffer.allocateDirect (capacity);
        newBytebuffer.put (byteBuffer);
        byteBuffer = newBytebuffer;
    }

    private int length(ByteBuffer value) {
        int position = value.position ();
        int limit = value.limit ();
        return limit - position;
    }

    public void put(ByteBuffer value) {
        int valueSize = length (value);
        cyclicExpansion (valueSize);
        byteBuffer.put (value);
    }

    public boolean tryGet(ByteBuffer value) {
        int valueSize = length (value);
        int length = length ();
        if (valueSize > length) {
            value.put (byteBuffer);
            return Boolean.FALSE;
        } else {
            int makeLimit = byteBuffer.limit ();
            int readSize = valueSize + byteBuffer.position ();
            byteBuffer.limit (readSize);
            value.put (byteBuffer);
            byteBuffer.limit (makeLimit);
            return Boolean.TRUE;
        }
    }

    public void putBytes(byte[] value) {
        cyclicExpansion (value.length);
        byteBuffer.put (value);
    }

    public void putByte(byte value) {
        automaticExpansion ();
        byteBuffer.put (value);
    }

    public void putShort(short value) {
        automaticExpansion ();
        byteBuffer.putShort (value);
    }

    public void putInt(int value) {
        automaticExpansion ();
        byteBuffer.putInt (value);
    }

    public void putLong(long value) {
        automaticExpansion ();
        byteBuffer.putLong (value);
    }

    public void putFloat(float value) {
        automaticExpansion ();
        byteBuffer.putFloat (value);
    }

    public void putDouble(double value) {
        automaticExpansion ();
        byteBuffer.putDouble (value);
    }

    public void putChar(char value) {
        automaticExpansion ();
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
