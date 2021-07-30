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
package com.github.thierrysquirrel.hummingbird.core.client.factory;

import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.builder.SocketChannelFacadeBuilder;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

/**
 * Classname: ClientSocketSelectorKeysFactory
 * Description:
 * Date: 2021/7/30 12:58
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
@Slf4j
public class ClientSocketSelectorKeysFactory {
    private ClientSocketSelectorKeysFactory() {
    }

    public static <T> void isConnectable(SocketChannel socketChannel, SelectionKey selectionKey, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture) throws IOException {
        socketChannel.finishConnect ();
        selectionKey.interestOps (selectionKey.interestOps () & ~SelectionKey.OP_CONNECT);
        selectionKey.interestOps (selectionKey.interestOps () | SelectionKey.OP_READ);
        completeSocketChannelFacade (socketChannel, hummingbirdEncoder, hummingbirdHandler, channelHeartbeatDomainCache, socketChannelFacadeCompletableFuture);
    }

    private static <T> void completeSocketChannelFacade(SocketChannel socketChannel, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture) throws IOException {
        SocketChannelFacade<T> socketChannelFacade = SocketChannelFacadeBuilder.builderSocketChannelFacade (hummingbirdEncoder, hummingbirdHandler, channelHeartbeatDomainCache, socketChannel);
        socketChannelFacadeCompletableFuture.complete (socketChannelFacade);
    }
}
