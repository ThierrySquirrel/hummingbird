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
package io.github.thierrysquirrel.hummingbird.core.server.init.factory;

import io.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import io.github.thierrysquirrel.hummingbird.core.server.factory.execution.ServerSocketSelectorExecution;
import io.github.thierrysquirrel.hummingbird.core.server.init.factory.constant.HummingbirdServerInitFactoryConstant;

import java.nio.channels.ServerSocketChannel;

/**
 * Classname: HummingbirdServerInitFactory
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HummingbirdServerInitFactory {
    private HummingbirdServerInitFactory() {
    }

    public static <T> void init(ServerSocketChannel serverSocketChannel, HummingbirdDomain<T> hummingbirdDomain) {
        for (int i = 0; i < HummingbirdServerInitFactoryConstant.THREADS_NUMBER; i++) {
            ServerSocketSelectorExecution.serverSocketSelector(serverSocketChannel, hummingbirdDomain);
        }
    }
}
