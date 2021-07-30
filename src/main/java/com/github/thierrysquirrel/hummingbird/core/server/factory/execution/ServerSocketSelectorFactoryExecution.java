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
package com.github.thierrysquirrel.hummingbird.core.server.factory.execution;

import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.factory.SocketSelectorFactory;
import com.github.thierrysquirrel.hummingbird.core.factory.constant.SocketSelectorFactoryConstant;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import com.github.thierrysquirrel.hummingbird.core.server.factory.ServerSocketSelectorFactory;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Classname: ServerSocketSelectorFactoryExecution
 * Description:
 * Date: 2021/7/29 21:46
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class ServerSocketSelectorFactoryExecution {
    private static boolean isRun = Boolean.TRUE;

    private ServerSocketSelectorFactoryExecution() {
    }

    public static <T> void serverSocketSelector(ServerSocketChannel serverSocketChannel, HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache) throws IOException {
        Selector selector = ServerSocketSelectorFactory.registerAcceptSelector (serverSocketChannel);
        int selectOffset = 0;
        while (isRun) {
            int select = SocketSelectorFactory.select (selector);
            if (select > 0) {
                selectOffset = 0;
                ServerSocketSelectorKeysFactoryExecution.serverSocketSelectorKeys (serverSocketChannel,hummingbirdDecoder,hummingbirdEncoder,hummingbirdHandler,channelHeartbeatDomainCache,selector);

            } else {
                selectOffset++;
                if (selectOffset >= SocketSelectorFactoryConstant.SELECT_OFFSET_MAX) {
                    selector = SocketSelectorFactory.repairSelector (selector);
                    selectOffset = 0;
                }

            }
        }

    }

    public static void stop() {
        isRun = Boolean.FALSE;
    }
}
