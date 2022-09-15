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
package com.github.thierrysquirrel.hummingbird.core.client.thread;

import com.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

/**
 * Classname: AbstractClientSocketSelectorThread
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
@Data
@Slf4j
public abstract class AbstractClientSocketSelectorThread<T> implements Runnable {
    private SocketChannel socketChannel;
    private HummingbirdDomain<T> hummingbirdDomain;
    private CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture;

    protected AbstractClientSocketSelectorThread(SocketChannel socketChannel, HummingbirdDomain<T> hummingbirdDomain, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture) {
        this.socketChannel = socketChannel;
        this.hummingbirdDomain = hummingbirdDomain;
        this.socketChannelFacadeCompletableFuture = socketChannelFacadeCompletableFuture;
    }

    /**
     * clientSocketSelector
     *
     * @param socketChannel                        socketChannel
     * @param hummingbirdDomain                    hummingbirdDomain
     * @param socketChannelFacadeCompletableFuture socketChannelFacadeCompletableFuture
     * @throws IOException IOException
     */
    protected abstract void clientSocketSelector(SocketChannel socketChannel, HummingbirdDomain<T> hummingbirdDomain, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture) throws IOException;

    @Override
    public void run() {
        try {
            clientSocketSelector(this.socketChannel, hummingbirdDomain, this.socketChannelFacadeCompletableFuture);
        } catch (Exception e) {
            log.error("clientSocketSelector Error", e);
        }
    }
}
