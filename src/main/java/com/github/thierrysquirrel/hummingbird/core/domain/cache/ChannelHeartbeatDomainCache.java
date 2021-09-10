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
package com.github.thierrysquirrel.hummingbird.core.domain.cache;

import com.github.thierrysquirrel.hummingbird.core.domain.ChannelHeartbeatDomain;
import com.github.thierrysquirrel.hummingbird.core.domain.builder.ChannelHeartbeatDomainBuilder;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Classname: ChannelHeartbeatDomainCache
 * Description:
 * Date: 2021/7/29 20:29
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class ChannelHeartbeatDomainCache<T> {
    private final Map<String, ChannelHeartbeatDomain<T>> heartbeatDomainMap = Maps.newConcurrentMap ();
    private final HummingbirdHandler<T> hummingbirdHandler;
    private final long readHeartbeatTime;
    private final long writeHeartbeatTime;

    public ChannelHeartbeatDomainCache(HummingbirdHandler<T> hummingbirdHandler, long readHeartbeatTime, long writeHeartbeatTime) {
        this.hummingbirdHandler = hummingbirdHandler;
        this.readHeartbeatTime = readHeartbeatTime;
        this.writeHeartbeatTime = writeHeartbeatTime;
    }

    public void readHeartbeat(SocketChannelFacade<T> socketChannelFacade) {
        ChannelHeartbeatDomain<T> channelHeartbeatDomain = getChannelHeartbeatDomain (socketChannelFacade);
        channelHeartbeatDomain.setReadHeartbeatTime (System.currentTimeMillis ());
    }

    public void writeHeartbeat(SocketChannelFacade<T> socketChannelFacade) {
        ChannelHeartbeatDomain<T> channelHeartbeatDomain = getChannelHeartbeatDomain (socketChannelFacade);
        channelHeartbeatDomain.setWriteHeartbeatTime (System.currentTimeMillis ());
    }

    public void heartbeat() {
        long thisTime = System.currentTimeMillis ();
        for (Map.Entry<String, ChannelHeartbeatDomain<T>> entry : heartbeatDomainMap.entrySet ()) {
            ChannelHeartbeatDomain<T> channelHeartbeatDomain = entry.getValue ();
            SocketChannelFacade<T> socketChannelFacade = channelHeartbeatDomain.getSocketChannelFacade ();

            if (readHeartbeatTime > 0) {
                long channelReadTime = channelHeartbeatDomain.getReadHeartbeatTime ();
                channelTimeout (thisTime, channelReadTime, readHeartbeatTime, socketChannelFacade);
            }
            if (writeHeartbeatTime > 0) {
                long channelWriteTime = channelHeartbeatDomain.getWriteHeartbeatTime ();
                channelTimeout (thisTime, channelWriteTime, writeHeartbeatTime, socketChannelFacade);
            }
        }
    }

    private void channelTimeout(long thisTimeout, long channelHeartbeatTime, long heartbeatTime, SocketChannelFacade<T> socketChannelFacade) {
        long beatTime = thisTimeout - channelHeartbeatTime;
        if (beatTime > heartbeatTime) {
            hummingbirdHandler.channelTimeout (socketChannelFacade);
        }
    }

    public void remove(String socketChannelString) {
        heartbeatDomainMap.remove (socketChannelString);
    }

    private ChannelHeartbeatDomain<T> getChannelHeartbeatDomain(SocketChannelFacade<T> socketChannelFacade) {
        return heartbeatDomainMap.computeIfAbsent (socketChannelFacade.getSocketChannel ().toString (), key -> ChannelHeartbeatDomainBuilder.builderChannelHeartbeatDomain (socketChannelFacade));
    }

}
