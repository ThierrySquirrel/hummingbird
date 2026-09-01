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
package io.github.thierrysquirrel.hummingbird.core.facade.builder;

import io.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import io.github.thierrysquirrel.hummingbird.core.coder.container.HummingbirdDecoderCache;
import io.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import io.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import io.github.thierrysquirrel.hummingbird.core.facade.cache.SocketChannelFacadeCache;
import io.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: SocketChannelFacadeBuilder
 * Description:
 * Date:2026/6/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class SocketChannelFacadeBuilder {

    private static final Logger logger = Logger.getLogger(SocketChannelFacadeBuilder.class.getName());

    private SocketChannelFacadeBuilder() {
    }

    public static <T> SocketChannelFacade<T> builderSocketChannelFacade(HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache, HummingbirdDecoderCache<T> hummingbirdDecoderCache, SocketChannel socketChannel) {
        SocketChannelFacade<T> socketChannelFacade = new SocketChannelFacade<>();
        socketChannelFacade.setHummingbirdEncoder(hummingbirdEncoder);
        socketChannelFacade.setHummingbirdHandler(hummingbirdHandler);
        socketChannelFacade.setChannelHeartbeatDomainCache(channelHeartbeatDomainCache);
        socketChannelFacade.setHummingbirdDecoderCache(hummingbirdDecoderCache);
        socketChannelFacade.setSocketChannel(socketChannel);
        try {
            socketChannelFacade.setRemoteAddress(socketChannel.getRemoteAddress());
            socketChannelFacade.setLocalAddress(socketChannel.getLocalAddress());
        } catch (IOException e) {
            String logMsg = "socketChannel getAddress Error";
            logger.log(Level.WARNING, logMsg, e);
        }

        SocketChannelFacadeCache.put(socketChannel.toString(), socketChannelFacade);
        return socketChannelFacade;
    }
}
