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
package com.github.thierrysquirrel.hummingbird.core.server.thread;

import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

/**
 * Classname: AbstractServerSocketSelectorThread
 * Description:
 * Date: 2021/7/29 21:48
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
@Data
@Slf4j
public abstract class AbstractServerSocketSelectorThread<T> implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private HummingbirdDecoder<T> hummingbirdDecoder;
    private HummingbirdEncoder<T> hummingbirdEncoder;
    private HummingbirdHandler<T> hummingbirdHandler;
    private ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache;

    protected AbstractServerSocketSelectorThread(ServerSocketChannel serverSocketChannel, HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache) {
        this.serverSocketChannel = serverSocketChannel;
        this.hummingbirdDecoder = hummingbirdDecoder;
        this.hummingbirdEncoder = hummingbirdEncoder;
        this.hummingbirdHandler = hummingbirdHandler;
        this.channelHeartbeatDomainCache = channelHeartbeatDomainCache;
    }

    /**
     * serverSocketSelector
     *
     * @param serverSocketChannel         serverSocketChannel
     * @param hummingbirdDecoder          hummingbirdDecoder
     * @param hummingbirdEncoder          hummingbirdEncoder
     * @param hummingbirdHandler          hummingbirdHandler
     * @param channelHeartbeatDomainCache channelHeartbeatDomainCache
     * @throws IOException IOException
     */
    protected abstract void serverSocketSelector(ServerSocketChannel serverSocketChannel, HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache) throws IOException;

    @Override
    public void run() {
        try {
            serverSocketSelector (this.serverSocketChannel, this.hummingbirdDecoder, this.hummingbirdEncoder,
                    this.hummingbirdHandler, this.channelHeartbeatDomainCache);
        } catch (Exception e) {
            log.error ("serverSocketSelector Error", e);
        }
    }
}
