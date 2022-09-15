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
package com.github.thierrysquirrel.hummingbird.core.server.factory.execution;

import com.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import com.github.thierrysquirrel.hummingbird.core.factory.SocketSelectorFactory;
import com.github.thierrysquirrel.hummingbird.core.server.factory.ServerSocketSelectorFactory;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Classname: ServerSocketSelectorFactoryExecution
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ServerSocketSelectorFactoryExecution {
    private static boolean isRun = Boolean.TRUE;

    private ServerSocketSelectorFactoryExecution() {
    }

    public static <T> void serverSocketSelector(ServerSocketChannel serverSocketChannel, HummingbirdDomain<T> hummingbirdDomain) throws IOException {
        Selector selector = ServerSocketSelectorFactory.registerAcceptSelector(serverSocketChannel);
        while (isRun) {
            int select = SocketSelectorFactory.select(selector);
            if (select > 0) {
                ServerSocketSelectorKeysFactoryExecution.serverSocketSelectorKeys(serverSocketChannel, hummingbirdDomain, selector);

            }
        }

    }

    public static void stop() {
        isRun = Boolean.FALSE;
    }
}
