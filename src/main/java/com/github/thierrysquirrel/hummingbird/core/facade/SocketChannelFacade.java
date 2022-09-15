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
package com.github.thierrysquirrel.hummingbird.core.facade;

import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.coder.container.HummingbirdDecoderCache;
import com.github.thierrysquirrel.hummingbird.core.container.SocketWriteStateContainer;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.facade.cache.ByteBufferFacadeChannelReadCache;
import com.github.thierrysquirrel.hummingbird.core.facade.cache.ByteBufferFacadeChannelWriteCache;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Classname: SocketChannelFacade
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
@Data
@Slf4j
public class SocketChannelFacade<T> {
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
                log.error("close Error", e);
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
}
