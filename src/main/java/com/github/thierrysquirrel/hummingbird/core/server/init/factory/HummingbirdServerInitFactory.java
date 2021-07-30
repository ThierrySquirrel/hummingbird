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
package com.github.thierrysquirrel.hummingbird.core.server.init.factory;

import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import com.github.thierrysquirrel.hummingbird.core.server.factory.execution.ServerSocketSelectorExecution;
import com.github.thierrysquirrel.hummingbird.core.server.init.factory.constant.HummingbirdServerInitFactoryConstant;

import java.nio.channels.ServerSocketChannel;

/**
 * Classname: HummingbirdServerInitFactory
 * Description:
 * Date: 2021/7/29 21:41
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class HummingbirdServerInitFactory {
    private HummingbirdServerInitFactory() {
    }

    public static <T> void init(ServerSocketChannel serverSocketChannel, HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder,
                                HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache) {
        for (int i = 0; i < HummingbirdServerInitFactoryConstant.THREADS_NUMBER; i++) {
            ServerSocketSelectorExecution.serverSocketSelector (serverSocketChannel, hummingbirdDecoder, hummingbirdEncoder, hummingbirdHandler, channelHeartbeatDomainCache);
        }
    }
}
