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
package io.github.thierrysquirrel.hummingbird.core.facade;

import io.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import io.github.thierrysquirrel.hummingbird.core.coder.container.HummingbirdDecoderCache;
import io.github.thierrysquirrel.hummingbird.core.container.SocketWriteStateContainer;
import io.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import io.github.thierrysquirrel.hummingbird.core.facade.cache.ByteBufferFacadeChannelReadCache;
import io.github.thierrysquirrel.hummingbird.core.facade.cache.ByteBufferFacadeChannelWriteCache;
import io.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: SocketChannelFacade
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class SocketChannelFacade<T> {

    private static final Logger logger = Logger.getLogger(SocketChannelFacade.class.getName());

    private HummingbirdEncoder<T> hummingbirdEncoder;
    private HummingbirdHandler<T> hummingbirdHandler;
    private ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache;
    private HummingbirdDecoderCache<T> hummingbirdDecoderCache;
    private SocketChannel socketChannel;
    private SocketAddress remoteAddress;
    private SocketAddress localAddress;

    public void sendMessage(T message) throws IOException {
        if (!isOpen()) {
            return;
        }
        channelHeartbeatDomainCache.writeHeartbeat(this);
        String socketChannelString = socketChannel.toString();
        SocketWriteStateContainer.writing(socketChannelString);

        ByteBufferFacade byteBufferFacade = ByteBufferFacadeChannelWriteCache.getByteBufferFacade(socketChannelString);
        hummingbirdEncoder.encoder(message, byteBufferFacade);
        byteBufferFacade.flip();
        while (true) {
            int write;
            try {
                write = socketChannel.write(byteBufferFacade.getByteBuffer());
            } catch (IOException e) {
                byteBufferFacade.clear();
                throw e;
            }
            if (write > 0 && byteBufferFacade.length() == 0) {
                break;
            }
        }
        byteBufferFacade.clear();
        SocketWriteStateContainer.finished(socketChannelString);

    }

    public void close() {
        String socketChannelString = socketChannel.toString();
        channelHeartbeatDomainCache.remove(socketChannelString);
        hummingbirdDecoderCache.remove(socketChannelString);
        ByteBufferFacadeChannelReadCache.removeByteBufferFacade(socketChannelString);
        ByteBufferFacadeChannelWriteCache.removeByteBufferFacade(socketChannelString);
        SocketWriteStateContainer.removeCache(socketChannelString);
        hummingbirdHandler.channelClose(remoteAddress, localAddress);
        if (isOpen()) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                String logMsg = "close Error";
                logger.log(Level.WARNING, logMsg, e);
            }
        }
    }

    public void putMessageDecoderCache(T messageDecoderCache) {
        hummingbirdDecoderCache.putMessageDecoderCache(socketChannel.toString(), messageDecoderCache);
    }

    public T getMessageDecoderCache() {
        return hummingbirdDecoderCache.getMessageDecoderCache(socketChannel.toString());
    }

    public void removeMessageDecoderCache() {
        hummingbirdDecoderCache.remove(socketChannel.toString());
    }

    public boolean isOpen() {
        return socketChannel.isOpen();
    }

    public HummingbirdEncoder<T> getHummingbirdEncoder() {
        return hummingbirdEncoder;
    }

    public void setHummingbirdEncoder(HummingbirdEncoder<T> hummingbirdEncoder) {
        this.hummingbirdEncoder = hummingbirdEncoder;
    }

    public HummingbirdHandler<T> getHummingbirdHandler() {
        return hummingbirdHandler;
    }

    public void setHummingbirdHandler(HummingbirdHandler<T> hummingbirdHandler) {
        this.hummingbirdHandler = hummingbirdHandler;
    }

    public ChannelHeartbeatDomainCache<T> getChannelHeartbeatDomainCache() {
        return channelHeartbeatDomainCache;
    }

    public void setChannelHeartbeatDomainCache(ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache) {
        this.channelHeartbeatDomainCache = channelHeartbeatDomainCache;
    }

    public HummingbirdDecoderCache<T> getHummingbirdDecoderCache() {
        return hummingbirdDecoderCache;
    }

    public void setHummingbirdDecoderCache(HummingbirdDecoderCache<T> hummingbirdDecoderCache) {
        this.hummingbirdDecoderCache = hummingbirdDecoderCache;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public SocketAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(SocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    @Override
    public String toString() {
        return "SocketChannelFacade{" +
                "hummingbirdEncoder=" + hummingbirdEncoder +
                ", hummingbirdHandler=" + hummingbirdHandler +
                ", channelHeartbeatDomainCache=" + channelHeartbeatDomainCache +
                ", hummingbirdDecoderCache=" + hummingbirdDecoderCache +
                ", socketChannel=" + socketChannel +
                ", remoteAddress=" + remoteAddress +
                ", localAddress=" + localAddress +
                '}';
    }
}
