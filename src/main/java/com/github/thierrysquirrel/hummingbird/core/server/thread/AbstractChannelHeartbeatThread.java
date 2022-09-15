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
package com.github.thierrysquirrel.hummingbird.core.server.thread;

import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Classname: AbstractChannelHeartbeatThread
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
@Data
@Slf4j
public abstract class AbstractChannelHeartbeatThread<T> implements Runnable {
    private ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache;

    protected AbstractChannelHeartbeatThread(ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache) {
        this.channelHeartbeatDomainCache = channelHeartbeatDomainCache;
    }

    /**
     * heartbeat
     *
     * @param channelHeartbeatDomainCache channelHeartbeatDomainCache
     */
    protected abstract void heartbeat(ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache);

    @Override
    public void run() {
        try {
            heartbeat(this.channelHeartbeatDomainCache);
        } catch (Exception e) {
            log.error("heartbeat Error", e);
        }
    }
}
