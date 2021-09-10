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

import com.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import com.github.thierrysquirrel.hummingbird.core.factory.SocketSelectorKeysFactory;
import com.github.thierrysquirrel.hummingbird.core.server.factory.ServerSocketSelectorKeysFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;

/**
 * Classname: ServerSocketSelectorKeysFactoryExecution
 * Description:
 * Date: 2021/7/29 22:08
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class ServerSocketSelectorKeysFactoryExecution {
    private ServerSocketSelectorKeysFactoryExecution() {
    }

    public static <T> void serverSocketSelectorKeys(ServerSocketChannel serverSocketChannel, HummingbirdDomain<T> hummingbirdDomain, Selector selector) throws IOException {
        Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys ().iterator ();
        while (selectionKeyIterator.hasNext ()) {
            SelectionKey selectionKey = SocketSelectorKeysFactory.getSelectionKey (selectionKeyIterator);
            if (Objects.isNull (selectionKey)) {
                return;
            }
            if (selectionKey.isAcceptable ()) {
                ServerSocketSelectorKeysFactory.isAcceptable (serverSocketChannel, selector);
                continue;
            }
            if (selectionKey.isReadable ()) {
                SocketSelectorKeysFactory.isReadable ((SocketChannel) selectionKey.channel (), hummingbirdDomain);
            }
        }
    }
}
