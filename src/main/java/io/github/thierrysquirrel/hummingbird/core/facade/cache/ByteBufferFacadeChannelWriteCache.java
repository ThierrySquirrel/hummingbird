/**
 * Copyright 2026/6/1 ThierrySquirrel
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
package io.github.thierrysquirrel.hummingbird.core.facade.cache;

import io.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import io.github.thierrysquirrel.hummingbird.core.facade.builder.ByteBufferFacadeBuilder;
import io.github.thierrysquirrel.jellyfish.concurrency.map.hash.ConcurrencyHashMap;


/**
 * Classname: ByteBufferFacadeChannelWriteCache
 * Description:
 * Date:2026/6/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class ByteBufferFacadeChannelWriteCache {
    private static final ConcurrencyHashMap<String, ByteBufferFacade> CHANNEL_WRITE_CACHE = new ConcurrencyHashMap<>();

    private ByteBufferFacadeChannelWriteCache() {
    }

    public static ByteBufferFacade getByteBufferFacade(String socketChannelString) {
        return CHANNEL_WRITE_CACHE.getIfAbsent(socketChannelString, key -> ByteBufferFacadeBuilder.builderDirectByteBufferFacade());
    }

    public static void removeByteBufferFacade(String socketChannelString) {
        ByteBufferFacade byteBufferFacade = CHANNEL_WRITE_CACHE.get(socketChannelString);
        if (byteBufferFacade != null) {
            byteBufferFacade.clear();
        }
        CHANNEL_WRITE_CACHE.deleteValue(socketChannelString);
    }

}
