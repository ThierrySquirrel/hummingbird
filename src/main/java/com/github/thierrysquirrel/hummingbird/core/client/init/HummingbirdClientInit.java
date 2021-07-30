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
package com.github.thierrysquirrel.hummingbird.core.client.init;

import com.github.thierrysquirrel.hummingbird.core.client.domain.HummingbirdClientDomain;
import com.github.thierrysquirrel.hummingbird.core.client.factory.ClientSocketChannelFactory;
import com.github.thierrysquirrel.hummingbird.core.client.factory.execution.ClientSocketSelectorExecution;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import com.github.thierrysquirrel.hummingbird.core.factory.SocketAddressFactory;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import lombok.Data;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Classname: HummingbirdClientInit
 * Description:
 * Date: 2021/7/30 12:19
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
@Data
public class HummingbirdClientInit<T> {
    private HummingbirdClientDomain<T> hummingbirdClientDomain;
    private CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture;

    public SocketChannelFacade<T> connect() throws ExecutionException, InterruptedException, IOException {
        socketChannelFacadeCompletableFuture = new CompletableFuture<> ();
        SocketChannel socketChannel = ClientSocketChannelFactory.connect (SocketAddressFactory.getInetSocketAddress (hummingbirdClientDomain.getUrl ()));

        HummingbirdHandler<T> hummingbirdHandler = hummingbirdClientDomain.getHummingbirdHandler ();
        ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache = new ChannelHeartbeatDomainCache<> (hummingbirdHandler, hummingbirdClientDomain.getReadHeartbeatTime (), hummingbirdClientDomain.getWriteHeartbeatTime ());
        ClientSocketSelectorExecution.clientSocketSelector (hummingbirdClientDomain.getHummingbirdClientThreadPool (), socketChannel, hummingbirdClientDomain.getHummingbirdDecoder (),
                hummingbirdClientDomain.getHummingbirdEncoder (), hummingbirdHandler,channelHeartbeatDomainCache,socketChannelFacadeCompletableFuture);
        return socketChannelFacadeCompletableFuture.get ();
    }
}
