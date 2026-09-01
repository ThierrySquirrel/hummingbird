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

package io.github.thierrysquirrel.hummingbird.core.ssl.engine.domain.container;

import io.github.thierrysquirrel.hummingbird.core.ssl.engine.domain.SslEngineDomain;
import io.github.thierrysquirrel.hummingbird.core.ssl.engine.domain.builder.SslEngineDomainBuilder;
import io.github.thierrysquirrel.jellyfish.concurrency.map.hash.ConcurrencyHashMap;

import java.nio.channels.SocketChannel;

/**
 * Classname: SslEngineDomainContainer
 * Description:
 * Date:2026/9/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class SslEngineDomainContainer {
    private SslEngineDomainContainer() {
    }

    public static final ConcurrencyHashMap<String, SslEngineDomain> SSL_ENGINE_DOMAIN_MAP = new ConcurrencyHashMap<>();

    public static SslEngineDomain getSslEngineDomain(SocketChannel socketChannel) {
        String channelString = socketChannel.toString();
        return SSL_ENGINE_DOMAIN_MAP.getIfAbsent(channelString, key -> createSslEngineDomain(socketChannel));
    }

    public static void deleteValue(SocketChannel socketChannel) {
        String channelString = socketChannel.toString();
        SslEngineDomain sslEngineDomain = SSL_ENGINE_DOMAIN_MAP.get(channelString);
        if (sslEngineDomain != null) {
            sslEngineDomain.getSocketEncryptRead().clear();
            SSL_ENGINE_DOMAIN_MAP.deleteValue(channelString);
        }
    }

    private static SslEngineDomain createSslEngineDomain(SocketChannel socketChannel) {
        return SslEngineDomainBuilder.builderHttpSslEngineDomain(socketChannel);
    }
}
