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

import io.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: AbstractChannelHeartbeatThread
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/

public abstract class AbstractChannelHeartbeatThread<T> implements Runnable {

    private static final Logger logger = Logger.getLogger(AbstractChannelHeartbeatThread.class.getName());

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
            String logMsg = "heartbeat Error";
            logger.log(Level.WARNING, logMsg, e);
        }
    }
}
