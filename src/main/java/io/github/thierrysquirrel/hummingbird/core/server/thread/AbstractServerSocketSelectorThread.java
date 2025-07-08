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
package io.github.thierrysquirrel.hummingbird.core.server.thread;

import io.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

/**
 * Classname: AbstractServerSocketSelectorThread
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
@Data
@Slf4j
public abstract class AbstractServerSocketSelectorThread<T> implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private HummingbirdDomain<T> hummingbirdDomain;

    protected AbstractServerSocketSelectorThread(ServerSocketChannel serverSocketChannel, HummingbirdDomain<T> hummingbirdDomain) {
        this.serverSocketChannel = serverSocketChannel;
        this.hummingbirdDomain = hummingbirdDomain;
    }

    /**
     * serverSocketSelector
     *
     * @param serverSocketChannel serverSocketChannel
     * @param hummingbirdDomain   hummingbirdDomain
     * @throws IOException IOException
     */
    protected abstract void serverSocketSelector(ServerSocketChannel serverSocketChannel, HummingbirdDomain<T> hummingbirdDomain) throws IOException;

    @Override
    public void run() {
        try {
            serverSocketSelector(this.serverSocketChannel, this.hummingbirdDomain);
        } catch (Exception e) {
            log.error("serverSocketSelector Error", e);
        }
    }
}
