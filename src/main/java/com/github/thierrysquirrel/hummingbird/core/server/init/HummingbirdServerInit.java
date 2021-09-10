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
package com.github.thierrysquirrel.hummingbird.core.server.init;

import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.coder.container.HummingbirdDecoderCache;
import com.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import com.github.thierrysquirrel.hummingbird.core.domain.builder.HummingbirdDomainBuilder;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.factory.SocketAddressFactory;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import com.github.thierrysquirrel.hummingbird.core.server.factory.ServerSocketChannelFactory;
import com.github.thierrysquirrel.hummingbird.core.server.factory.execution.ChannelHeartbeatExecution;
import com.github.thierrysquirrel.hummingbird.core.server.init.factory.HummingbirdServerInitFactory;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

/**
 * Classname: HummingbirdServerInit
 * Description:
 * Date: 2021/7/29 21:11
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class HummingbirdServerInit {

    private HummingbirdServerInit() {
    }

    public static <T> void init(String url, long readHeartbeatTime, long writeHeartbeatTime,
                                HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannelFactory.bind (SocketAddressFactory.getInetSocketAddress (url));
        ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache = new ChannelHeartbeatDomainCache<> (hummingbirdHandler, readHeartbeatTime, writeHeartbeatTime);
        ChannelHeartbeatExecution.channelHeartbeat (channelHeartbeatDomainCache);
        HummingbirdDecoderCache<T> hummingbirdDecoderCache = new HummingbirdDecoderCache<> ();
        HummingbirdDomain<T> hummingbirdDomain = HummingbirdDomainBuilder.builderHummingbirdDomain (hummingbirdDecoder, hummingbirdEncoder, hummingbirdHandler, channelHeartbeatDomainCache, hummingbirdDecoderCache);
        HummingbirdServerInitFactory.init (serverSocketChannel, hummingbirdDomain);
    }
}
