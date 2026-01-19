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
package io.github.thierrysquirrel.hummingbird.core.client.factory;

import io.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import io.github.thierrysquirrel.hummingbird.core.coder.container.HummingbirdDecoderCache;
import io.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import io.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import io.github.thierrysquirrel.hummingbird.core.facade.builder.SocketChannelFacadeBuilder;
import io.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

/**
 * Classname: ClientSocketSelectorKeysFactory
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ClientSocketSelectorKeysFactory {
    private ClientSocketSelectorKeysFactory() {
    }

    public static <T> void isConnectable(SocketChannel socketChannel, SelectionKey selectionKey, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache, HummingbirdDecoderCache<T> hummingbirdDecoderCache, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture) throws IOException {
        socketChannel.finishConnect();
        selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_CONNECT);
        selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_READ);
        completeSocketChannelFacade(socketChannel, hummingbirdEncoder, hummingbirdHandler, channelHeartbeatDomainCache, hummingbirdDecoderCache, socketChannelFacadeCompletableFuture);
    }

    private static <T> void completeSocketChannelFacade(SocketChannel socketChannel, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache, HummingbirdDecoderCache<T> hummingbirdDecoderCache, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture) throws IOException {
        SocketChannelFacade<T> socketChannelFacade = SocketChannelFacadeBuilder.builderSocketChannelFacade(hummingbirdEncoder, hummingbirdHandler, channelHeartbeatDomainCache, hummingbirdDecoderCache, socketChannel);
        socketChannelFacadeCompletableFuture.complete(socketChannelFacade);
    }
}
