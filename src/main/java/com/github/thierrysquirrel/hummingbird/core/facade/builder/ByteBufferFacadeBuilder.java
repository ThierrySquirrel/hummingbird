/**
 * Copyright 2024/8/8 ThierrySquirrel
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
 **/
package com.github.thierrysquirrel.hummingbird.core.facade.builder;

import com.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.constant.ByteBufferFacadeConstant;

import java.nio.ByteBuffer;

/**
 * Classname: ByteBufferFacadeBuilder
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ByteBufferFacadeBuilder {
    private ByteBufferFacadeBuilder() {
    }

    public static ByteBufferFacade builderDirectByteBufferFacade() {
        ByteBufferFacade byteBufferFacade = new ByteBufferFacade();
        byteBufferFacade.setByteBuffer(ByteBuffer.allocateDirect(ByteBufferFacadeConstant.INIT_SIZE));
        return byteBufferFacade;
    }

    public static ByteBufferFacade builderByteBufferFacade() {
        ByteBufferFacade byteBufferFacade = new ByteBufferFacade();
        byteBufferFacade.setByteBuffer(ByteBuffer.allocate(ByteBufferFacadeConstant.INIT_SIZE));
        return byteBufferFacade;
    }

    public static ByteBufferFacade builderByteBufferFacade(ByteBuffer byteBuffer) {
        ByteBufferFacade byteBufferFacade = new ByteBufferFacade();
        byteBufferFacade.setByteBuffer(byteBuffer);
        return byteBufferFacade;
    }
}
