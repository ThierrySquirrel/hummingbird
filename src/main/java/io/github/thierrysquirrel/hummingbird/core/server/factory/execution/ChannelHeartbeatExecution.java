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
package io.github.thierrysquirrel.hummingbird.core.server.factory.execution;

import io.github.thierrysquirrel.hummingbird.core.builder.constant.ThreadPoolExecutorBuilderConstant;
import io.github.thierrysquirrel.hummingbird.core.builder.container.ThreadPoolExecutorContainer;
import io.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import io.github.thierrysquirrel.hummingbird.core.server.thread.execute.ChannelHeartbeatThreadExecute;

/**
 * Classname: ChannelHeartbeatExecution
 * Description:
 * Date:2026/6/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class ChannelHeartbeatExecution {
    private ChannelHeartbeatExecution() {
    }

    public static <T> void channelHeartbeat(String url, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache) {
        ChannelHeartbeatThreadExecute<T> channelHeartbeatThreadExecute = new ChannelHeartbeatThreadExecute<>(channelHeartbeatDomainCache);
        ThreadPoolExecutorContainer.getChannelHeartbeat(url).execute(channelHeartbeatThreadExecute, ThreadPoolExecutorBuilderConstant.CHANNEL_HEARTBEAT_DELAY);
    }
}
