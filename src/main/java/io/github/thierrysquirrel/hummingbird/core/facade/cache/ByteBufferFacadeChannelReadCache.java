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
package io.github.thierrysquirrel.hummingbird.core.facade.cache;

import io.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import io.github.thierrysquirrel.hummingbird.core.facade.builder.ByteBufferFacadeBuilder;
import io.github.thierrysquirrel.jellyfish.concurrency.map.hash.ConcurrencyHashMap;

import java.util.Objects;


/**
 * Classname: ByteBufferFacadeChannelReadCache
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ByteBufferFacadeChannelReadCache {
    private static final ConcurrencyHashMap<String, ByteBufferFacade> CHANNEL_READ_CACHE = new ConcurrencyHashMap<>(Runtime.getRuntime().availableProcessors() * 2);

    private ByteBufferFacadeChannelReadCache() {
    }

    public static ByteBufferFacade getByteBufferFacade(String socketChannelString) {
        ByteBufferFacade value = CHANNEL_READ_CACHE.get(socketChannelString);
        if (Objects.isNull(value)) {
            value = ByteBufferFacadeBuilder.builderDirectByteBufferFacade();
            CHANNEL_READ_CACHE.set(socketChannelString, value);
        }
        return value;
    }

    public static void removeByteBufferFacade(String socketChannelString) {
        CHANNEL_READ_CACHE.deleteValue(socketChannelString);
    }
}
