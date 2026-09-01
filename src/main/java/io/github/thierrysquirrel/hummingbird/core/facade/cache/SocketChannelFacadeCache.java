/**
 * Copyright 2026/9/1 ThierrySquirrel
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

import io.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import io.github.thierrysquirrel.jellyfish.concurrency.map.hash.ConcurrencyHashMap;

/**
 * Classname: SocketChannelFacadeCache
 * Description:
 * Date:2026/9/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class SocketChannelFacadeCache {
    private SocketChannelFacadeCache() {
    }

    public static final ConcurrencyHashMap<String, SocketChannelFacade<?>> SOCKET_CHANNEL_FACADE_MAP = new ConcurrencyHashMap<>();

    public static void put(String channelString, SocketChannelFacade<?> socketChannelFacade) {
        SOCKET_CHANNEL_FACADE_MAP.set(channelString, socketChannelFacade);
    }

    public static SocketChannelFacade<?> get(String channelString) {
        return SOCKET_CHANNEL_FACADE_MAP.get(channelString);
    }

    public static void deleteValue(String channelString) {
        SOCKET_CHANNEL_FACADE_MAP.deleteValue(channelString);
    }

}
