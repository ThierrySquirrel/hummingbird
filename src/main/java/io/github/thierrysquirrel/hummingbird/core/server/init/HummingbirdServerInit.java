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
package io.github.thierrysquirrel.hummingbird.core.server.init;

import io.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import io.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import io.github.thierrysquirrel.hummingbird.core.coder.container.HummingbirdDecoderCache;
import io.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import io.github.thierrysquirrel.hummingbird.core.domain.builder.HummingbirdDomainBuilder;
import io.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import io.github.thierrysquirrel.hummingbird.core.factory.SocketAddressFactory;
import io.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import io.github.thierrysquirrel.hummingbird.core.server.factory.ServerSocketChannelFactory;
import io.github.thierrysquirrel.hummingbird.core.server.factory.execution.ChannelHeartbeatExecution;
import io.github.thierrysquirrel.hummingbird.core.server.init.factory.HummingbirdServerInitFactory;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

/**
 * Classname: HummingbirdServerInit
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HummingbirdServerInit {

    private HummingbirdServerInit() {
    }

    public static <T> void init(String url, long readHeartbeatTime, long writeHeartbeatTime,
                                HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannelFactory.bind(SocketAddressFactory.getInetSocketAddress(url));
        ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache = new ChannelHeartbeatDomainCache<>(hummingbirdHandler, readHeartbeatTime, writeHeartbeatTime);
        ChannelHeartbeatExecution.channelHeartbeat(channelHeartbeatDomainCache);
        HummingbirdDecoderCache<T> hummingbirdDecoderCache = new HummingbirdDecoderCache<>();
        HummingbirdDomain<T> hummingbirdDomain = HummingbirdDomainBuilder.builderHummingbirdDomain(hummingbirdDecoder, hummingbirdEncoder, hummingbirdHandler, channelHeartbeatDomainCache, hummingbirdDecoderCache);
        HummingbirdServerInitFactory.init(serverSocketChannel, hummingbirdDomain);
    }
}
