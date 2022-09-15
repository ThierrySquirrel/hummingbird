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
package com.github.thierrysquirrel.hummingbird.core.client.factory.execution;

import com.github.thierrysquirrel.hummingbird.core.client.factory.ClientSocketSelectorFactory;
import com.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import com.github.thierrysquirrel.hummingbird.core.factory.SocketSelectorFactory;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

/**
 * Classname: ClientSocketSelectorFactoryExecution
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ClientSocketSelectorFactoryExecution {
    private ClientSocketSelectorFactoryExecution() {
    }

    public static <T> void clientSocketSelector(SocketChannel socketChannel, HummingbirdDomain<T> hummingbirdDomain, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture) throws IOException {

        Selector selector = ClientSocketSelectorFactory.registerConnectSelector(socketChannel);
        while (true) {
            int select = SocketSelectorFactory.select(selector);
            if (!socketChannel.isOpen()) {
                break;
            }
            hummingbirdDomain.getChannelHeartbeatDomainCache().heartbeat();

            if (select > 0) {
                ClientSocketSelectorKeysFactoryExecution.clientSocketSelectorKeys(socketChannel, hummingbirdDomain, socketChannelFacadeCompletableFuture, selector);
            }
        }
        ClientSocketSelectorFactory.close(selector, socketChannel);
    }
}
