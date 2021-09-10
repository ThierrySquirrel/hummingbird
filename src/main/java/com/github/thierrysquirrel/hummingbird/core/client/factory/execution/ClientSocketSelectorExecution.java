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
package com.github.thierrysquirrel.hummingbird.core.client.factory.execution;

import com.github.thierrysquirrel.hummingbird.core.client.thread.execute.ClientSocketSelectorThreadExecute;
import com.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;

import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Classname: ClientSocketSelectorExecution
 * Description:
 * Date: 2021/7/30 12:40
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class ClientSocketSelectorExecution {
    private ClientSocketSelectorExecution() {
    }

    public static <T> void clientSocketSelector(ThreadPoolExecutor hummingbirdClientThreadPool, SocketChannel socketChannel, HummingbirdDomain<T> hummingbirdDomain, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture) {
        ClientSocketSelectorThreadExecute<T> clientSocketSelectorThreadExecute = new ClientSocketSelectorThreadExecute<> (socketChannel, hummingbirdDomain, socketChannelFacadeCompletableFuture);
        hummingbirdClientThreadPool.execute (clientSocketSelectorThreadExecute);
    }
}
