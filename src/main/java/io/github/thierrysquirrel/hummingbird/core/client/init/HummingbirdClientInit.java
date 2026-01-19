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
package io.github.thierrysquirrel.hummingbird.core.client.init;

import io.github.thierrysquirrel.hummingbird.core.client.factory.ClientSocketChannelFactory;
import io.github.thierrysquirrel.hummingbird.core.client.factory.execution.ClientSocketSelectorExecution;
import io.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import io.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import io.github.thierrysquirrel.hummingbird.core.factory.SocketAddressFactory;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Classname: HummingbirdClientInit
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HummingbirdClientInit<T> {
    private ThreadPoolExecutor hummingbirdClientThreadPool;
    private String url;
    private HummingbirdDomain<T> hummingbirdDomain;
    private CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture;

    public SocketChannelFacade<T> connect() throws ExecutionException, InterruptedException, IOException {
        socketChannelFacadeCompletableFuture = new CompletableFuture<>();
        SocketChannel socketChannel = ClientSocketChannelFactory.connect(SocketAddressFactory.getInetSocketAddress(url));

        ClientSocketSelectorExecution.clientSocketSelector(hummingbirdClientThreadPool, socketChannel, hummingbirdDomain, socketChannelFacadeCompletableFuture);
        return socketChannelFacadeCompletableFuture.get();
    }

    public ThreadPoolExecutor getHummingbirdClientThreadPool() {
        return hummingbirdClientThreadPool;
    }

    public void setHummingbirdClientThreadPool(ThreadPoolExecutor hummingbirdClientThreadPool) {
        this.hummingbirdClientThreadPool = hummingbirdClientThreadPool;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HummingbirdDomain<T> getHummingbirdDomain() {
        return hummingbirdDomain;
    }

    public void setHummingbirdDomain(HummingbirdDomain<T> hummingbirdDomain) {
        this.hummingbirdDomain = hummingbirdDomain;
    }

    public CompletableFuture<SocketChannelFacade<T>> getSocketChannelFacadeCompletableFuture() {
        return socketChannelFacadeCompletableFuture;
    }

    public void setSocketChannelFacadeCompletableFuture(CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture) {
        this.socketChannelFacadeCompletableFuture = socketChannelFacadeCompletableFuture;
    }

    @Override
    public String toString() {
        return "HummingbirdClientInit{" +
                "hummingbirdClientThreadPool=" + hummingbirdClientThreadPool +
                ", url='" + url + '\'' +
                ", hummingbirdDomain=" + hummingbirdDomain +
                ", socketChannelFacadeCompletableFuture=" + socketChannelFacadeCompletableFuture +
                '}';
    }
}
