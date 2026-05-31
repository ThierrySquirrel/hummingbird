/**
 * Copyright 2026/6/1 ThierrySquirrel
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

package io.github.thierrysquirrel.hummingbird.core.builder.container;

import io.github.thierrysquirrel.hummingbird.core.builder.ThreadPoolExecutorBuilder;
import io.github.thierrysquirrel.hummingbird.core.builder.constant.ThreadPoolExecutorBuilderConstant;
import io.github.thierrysquirrel.jellyfish.concurrency.map.hash.ConcurrencyHashMap;
import io.github.thierrysquirrel.jellyfish.thread.pool.ThreadPool;
import io.github.thierrysquirrel.jellyfish.thread.scheduled.one.ThreadScheduledOne;

/**
 * Classname: ThreadPoolExecutorContainer
 * Description:
 * Date:2026/6/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class ThreadPoolExecutorContainer {

    private ThreadPoolExecutorContainer() {
    }

    private static final ConcurrencyHashMap<String, ThreadScheduledOne> CHANNEL_HEARTBEAT_MAP = new ConcurrencyHashMap<>(ThreadPoolExecutorBuilderConstant.HUMMINGBIRD_SERVER_CORE_POOL_SIZE);
    private static final ConcurrencyHashMap<String, ThreadPool> HUMMINGBIRD_SERVER_MAP = new ConcurrencyHashMap<>(ThreadPoolExecutorBuilderConstant.HUMMINGBIRD_SERVER_CORE_POOL_SIZE);

    public static ThreadScheduledOne getChannelHeartbeat(String url) {
        return CHANNEL_HEARTBEAT_MAP.getIfAbsent(url, key -> ThreadPoolExecutorBuilder.builderChannelHeartbeatThreadPoolExecutor());
    }

    public static ThreadPool getHummingbirdServer(String url) {
        return HUMMINGBIRD_SERVER_MAP.getIfAbsent(url, key -> ThreadPoolExecutorBuilder.builderHummingbirdServerThreadPoolExecutor());
    }

}
