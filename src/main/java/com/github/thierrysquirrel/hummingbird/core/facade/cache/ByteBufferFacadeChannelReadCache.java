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
package com.github.thierrysquirrel.hummingbird.core.facade.cache;

import com.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.builder.ByteBufferFacadeBuilder;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Classname: ByteBufferFacadeChannelReadCache
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ByteBufferFacadeChannelReadCache {
    private static final Map<String, ByteBufferFacade> CHANNEL_READ_CACHE = Maps.newConcurrentMap();

    private ByteBufferFacadeChannelReadCache() {
    }

    public static ByteBufferFacade getByteBufferFacade(String socketChannelString) {
        return CHANNEL_READ_CACHE.computeIfAbsent(socketChannelString, key -> ByteBufferFacadeBuilder.builderDirectByteBufferFacade());
    }

    public static void removeByteBufferFacade(String socketChannelString) {
        CHANNEL_READ_CACHE.remove(socketChannelString);
    }
}
