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

import com.github.thierrysquirrel.hummingbird.core.client.factory.ClientSocketSelectorFactory;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import com.github.thierrysquirrel.hummingbird.core.factory.SocketSelectorFactory;
import com.github.thierrysquirrel.hummingbird.core.factory.constant.SocketSelectorFactoryConstant;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;

/**
 * Classname: ClientSocketSelectorFactoryExecution
 * Description:
 * Date: 2021/7/30 12:38
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class ClientSocketSelectorFactoryExecution {
    private ClientSocketSelectorFactoryExecution() {
    }

    public static <T> void clientSocketSelector(SocketChannel socketChannel, HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder,
                                                HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture) throws IOException {

        Selector selector = ClientSocketSelectorFactory.registerConnectSelector (socketChannel);
        int selectOffset = 0;
        while (true) {
            int select = SocketSelectorFactory.select (selector);

            if (!socketChannel.isOpen ()) {
                break;
            }
            channelHeartbeatDomainCache.heartbeat ();

            if(select>0){
                selectOffset=0;
                ClientSocketSelectorKeysFactoryExecution.clientSocketSelectorKeys (socketChannel,hummingbirdDecoder,hummingbirdEncoder,hummingbirdHandler,channelHeartbeatDomainCache,socketChannelFacadeCompletableFuture,selector);
            }else {
                selectOffset++;
                if(selectOffset> SocketSelectorFactoryConstant.SELECT_OFFSET_MAX){
                    selector = SocketSelectorFactory.repairSelector (selector);
                    selectOffset = 0;
                }
            }
        }
        ClientSocketSelectorFactory.close (selector, socketChannel);
    }
}
