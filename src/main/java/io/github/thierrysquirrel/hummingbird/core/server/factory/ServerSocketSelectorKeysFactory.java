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
package io.github.thierrysquirrel.hummingbird.core.server.factory;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.*;

/**
 * Classname: ServerSocketSelectorKeysFactory
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
@Slf4j
public class ServerSocketSelectorKeysFactory {
    private ServerSocketSelectorKeysFactory() {
    }

    public static void isAcceptable(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        var accept = serverSocketChannel.accept();
        if (accept != null) {
            accept.configureBlocking(Boolean.FALSE);
            accept.register(selector, SelectionKey.OP_READ);
        }
    }

}
