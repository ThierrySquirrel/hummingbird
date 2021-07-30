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

import com.github.thierrysquirrel.hummingbird.core.client.factory.ClientSocketSelectorKeysFactory;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import com.github.thierrysquirrel.hummingbird.core.factory.SocketSelectorKeysFactory;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

/**
 * Classname: ClientSocketSelectorKeysFactoryExecution
 * Description:
 * Date: 2021/7/30 12:58
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class ClientSocketSelectorKeysFactoryExecution {
    private ClientSocketSelectorKeysFactoryExecution() {
    }
    public static <T> void clientSocketSelectorKeys(SocketChannel socketChannel, HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder,
                                                    HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache, CompletableFuture<SocketChannelFacade<T>> socketChannelFacadeCompletableFuture, Selector selector) throws IOException {
        Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys ().iterator ();
        while (selectionKeyIterator.hasNext ()){
            SelectionKey selectionKey = selectionKeyIterator.next ();
            selectionKeyIterator.remove ();
            if(selectionKey.isConnectable ()){
                ClientSocketSelectorKeysFactory.isConnectable (socketChannel,selectionKey,hummingbirdEncoder,hummingbirdHandler,channelHeartbeatDomainCache,socketChannelFacadeCompletableFuture);
                break;
            }
            if(selectionKey.isReadable ()){
                SocketSelectorKeysFactory.isReadable (socketChannel,hummingbirdDecoder,hummingbirdEncoder,hummingbirdHandler,channelHeartbeatDomainCache);
            }
        }
    }
}
