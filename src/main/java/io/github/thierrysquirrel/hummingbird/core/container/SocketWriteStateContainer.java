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
package io.github.thierrysquirrel.hummingbird.core.container;

import io.github.thierrysquirrel.jellyfish.concurrency.map.hash.ConcurrencyHashMap;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Classname: SocketWriteStateContainer
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class SocketWriteStateContainer {
    private SocketWriteStateContainer() {
    }

    private static final ConcurrencyHashMap<String, ReentrantLock> SOCKET_WRITE_STATE = new ConcurrencyHashMap<>(Runtime.getRuntime().availableProcessors() * 2);

    public static void writing(String socketChannelString) {
        ReentrantLock reentrantLock = SOCKET_WRITE_STATE.get(socketChannelString);

        if (Objects.isNull(reentrantLock)) {
            reentrantLock = new ReentrantLock();
            SOCKET_WRITE_STATE.set(socketChannelString, reentrantLock);
        }
        reentrantLock.lock();
    }

    public static void finished(String socketChannelString) {
        SOCKET_WRITE_STATE.get(socketChannelString).unlock();

    }

    public static void removeCache(String socketChannelString) {
        SOCKET_WRITE_STATE.deleteValue(socketChannelString);
    }

}
