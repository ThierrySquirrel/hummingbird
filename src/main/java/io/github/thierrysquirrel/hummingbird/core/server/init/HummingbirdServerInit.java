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
package io.github.thierrysquirrel.hummingbird.core.server.init;

import io.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import io.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import io.github.thierrysquirrel.hummingbird.core.coder.container.HummingbirdDecoderCache;
import io.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import io.github.thierrysquirrel.hummingbird.core.domain.builder.HummingbirdDomainBuilder;
import io.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.server.HttpServerDecoder;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.server.HttpServerEncoder;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import io.github.thierrysquirrel.hummingbird.core.factory.SocketAddressFactory;
import io.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import io.github.thierrysquirrel.hummingbird.core.server.factory.ServerSocketChannelFactory;
import io.github.thierrysquirrel.hummingbird.core.server.factory.execution.ChannelHeartbeatExecution;
import io.github.thierrysquirrel.hummingbird.core.server.init.factory.HummingbirdServerInitFactory;
import io.github.thierrysquirrel.hummingbird.core.ssl.container.SslContainer;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * Classname: HummingbirdServerInit
 * Description:
 * Date:2026/6/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class HummingbirdServerInit {

    private HummingbirdServerInit() {
    }

    public static <T> void init(String url, long readHeartbeatTime, long writeHeartbeatTime,
                                HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler) {
        ServerSocketChannel serverSocketChannel = ServerSocketChannelFactory.bind(SocketAddressFactory.getInetSocketAddress(url));
        ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache = new ChannelHeartbeatDomainCache<>(hummingbirdHandler, readHeartbeatTime, writeHeartbeatTime);
        ChannelHeartbeatExecution.channelHeartbeat(url, channelHeartbeatDomainCache);
        HummingbirdDecoderCache<T> hummingbirdDecoderCache = new HummingbirdDecoderCache<>();
        HummingbirdDomain<T> hummingbirdDomain = HummingbirdDomainBuilder.builderHummingbirdDomain(hummingbirdDecoder, hummingbirdEncoder, hummingbirdHandler, channelHeartbeatDomainCache, hummingbirdDecoderCache);
        HummingbirdServerInitFactory.init(url, serverSocketChannel, hummingbirdDomain);
    }

    public static void initHttp(String url, long readHeartbeatTime, long writeHeartbeatTime,
                                HummingbirdHandler<HttpRequestContext> hummingbirdHandler) {
        init(url, readHeartbeatTime, writeHeartbeatTime, new HttpServerDecoder(), new HttpServerEncoder(), hummingbirdHandler);
    }

    public static void initHttps(String url, long readHeartbeatTime, long writeHeartbeatTime,
                                 HummingbirdHandler<HttpRequestContext> hummingbirdHandler, String resourcesFileName, String password) {

        InetSocketAddress inetSocketAddress = SocketAddressFactory.getInetSocketAddress(url);
        String ipAndPort = SocketAddressFactory.getIpAndPort(inetSocketAddress);
        SslContainer.setHttpSslContext(ipAndPort, resourcesFileName, password);

        init(url, readHeartbeatTime, writeHeartbeatTime, new HttpServerDecoder(), new HttpServerEncoder(), hummingbirdHandler);
    }
}
