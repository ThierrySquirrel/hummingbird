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
package com.github.thierrysquirrel.hummingbird.core.server.factory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Classname: ServerSocketSelectorFactory
 * Description:
 * Date: 2021/7/29 21:46
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class ServerSocketSelectorFactory {
    private ServerSocketSelectorFactory() {
    }
    public static Selector registerAcceptSelector(ServerSocketChannel serverSocketChannel) throws IOException {
        var selector = Selector.open ();
        serverSocketChannel.register (selector, SelectionKey.OP_ACCEPT);
        return selector;
    }
}
