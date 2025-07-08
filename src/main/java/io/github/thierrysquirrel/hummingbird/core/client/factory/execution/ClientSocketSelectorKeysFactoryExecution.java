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
package io.github.thierrysquirrel.hummingbird.core.client.factory.execution;

import io.github.thierrysquirrel.hummingbird.core.client.factory.ClientSocketSelectorKeysFactory;
import io.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import io.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import io.github.thierrysquirrel.hummingbird.core.factory.SocketSelectorKeysFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Classname: ClientSocketSelectorKeysFactoryExecution
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ClientSocketSelectorKeysFactoryExecution {
    private ClientSocketSelectorKeysFactoryExecution() {
    }

    public static <T> void clientSocketSelectorKeys(SocketChannel socketChannel, HummingbirdDomain<T> hummingbirdDomain, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture, Selector selector) throws IOException {
        Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
        while (selectionKeyIterator.hasNext()) {
            SelectionKey selectionKey = SocketSelectorKeysFactory.getSelectionKey(selectionKeyIterator);
            if (Objects.isNull(selectionKey)) {
                return;
            }
            if (selectionKey.isConnectable()) {
                ClientSocketSelectorKeysFactory.isConnectable(socketChannel, selectionKey, hummingbirdDomain.getHummingbirdEncoder(), hummingbirdDomain.getHummingbirdHandler(), hummingbirdDomain.getChannelHeartbeatDomainCache(), hummingbirdDomain.getHummingbirdDecoderCache(), socketChannelFacadeCompletableFuture);
                continue;
            }
            if (selectionKey.isReadable()) {
                SocketSelectorKeysFactory.isReadable(socketChannel, hummingbirdDomain);
            }
        }
    }
}
