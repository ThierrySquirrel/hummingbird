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
package io.github.thierrysquirrel.hummingbird.core.domain;

import io.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;

/**
 * Classname: ChannelHeartbeatDomain
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ChannelHeartbeatDomain<T> {
    private SocketChannelFacade<T> socketChannelFacade;
    private long readHeartbeatTime;
    private long writeHeartbeatTime;

    public SocketChannelFacade<T> getSocketChannelFacade() {
        return socketChannelFacade;
    }

    public void setSocketChannelFacade(SocketChannelFacade<T> socketChannelFacade) {
        this.socketChannelFacade = socketChannelFacade;
    }

    public long getReadHeartbeatTime() {
        return readHeartbeatTime;
    }

    public void setReadHeartbeatTime(long readHeartbeatTime) {
        this.readHeartbeatTime = readHeartbeatTime;
    }

    public long getWriteHeartbeatTime() {
        return writeHeartbeatTime;
    }

    public void setWriteHeartbeatTime(long writeHeartbeatTime) {
        this.writeHeartbeatTime = writeHeartbeatTime;
    }

    @Override
    public String toString() {
        return "ChannelHeartbeatDomain{" +
                "socketChannelFacade=" + socketChannelFacade +
                ", readHeartbeatTime=" + readHeartbeatTime +
                ", writeHeartbeatTime=" + writeHeartbeatTime +
                '}';
    }
}
