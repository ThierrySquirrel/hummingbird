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
package com.github.thierrysquirrel.hummingbird.core.container;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Classname: SocketWriteStateContainer
 * Description:
 * Date: 2021/9/10 17:56
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class SocketWriteStateContainer {
    private SocketWriteStateContainer() {
    }

    private static final Map<String, ReentrantLock> SOCKET_WRITE_STATE = Maps.newConcurrentMap ();

    public static void writing(String socketChannelString) {
        SOCKET_WRITE_STATE.computeIfAbsent (socketChannelString, key -> new ReentrantLock ()).lock ();
    }

    public static void finished(String socketChannelString) {
        SOCKET_WRITE_STATE.get (socketChannelString).unlock ();

    }

    public static void removeCache(String socketChannelString) {
        SOCKET_WRITE_STATE.remove (socketChannelString);
    }

}
