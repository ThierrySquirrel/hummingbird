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
package com.github.thierrysquirrel.hummingbird.core.factory;

import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.builder.SocketChannelFacadeBuilder;
import com.github.thierrysquirrel.hummingbird.core.facade.cache.ByteBufferFacadeChannelReadCache;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import com.github.thierrysquirrel.hummingbird.core.server.factory.constant.ServerSocketSelectorKeysFactoryConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Classname: SocketSelectorKeysFactory
 * Description:
 * Date: 2021/7/30 13:25
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
@Slf4j
public class SocketSelectorKeysFactory {
    private SocketSelectorKeysFactory() {
    }

    public static <T> void isReadable(SocketChannel socketChannel, HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache) throws IOException {
        SocketChannelFacade<T> socketChannelFacade = SocketChannelFacadeBuilder.builderSocketChannelFacade (hummingbirdEncoder, hummingbirdHandler, channelHeartbeatDomainCache, socketChannel);

        int readOffsetInit = ServerSocketSelectorKeysFactoryConstant.READ_OFFSET_INIT;
        int readOffset = readOffsetInit;

        ByteBufferFacade byteBufferFacade = ByteBufferFacadeChannelReadCache.getByteBufferFacade ();
        while (readOffset == readOffsetInit || readOffset > 0) {
            try {
                channelHeartbeatDomainCache.readHeartbeat (socketChannelFacade);
                readOffset = socketChannel.read (byteBufferFacade.getByteBuffer ());
                read (hummingbirdDecoder, hummingbirdHandler, socketChannelFacade, byteBufferFacade);
            } catch (IOException e) {
                log.error ("read Error", e);
                break;
            }
        }
        if (readOffset < 0) {
            close (socketChannelFacade, byteBufferFacade);
        }
    }

    private static <T> void read(HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdHandler<T> hummingbirdHandler, SocketChannelFacade<T> socketChannelFacade, ByteBufferFacade byteBufferFacade) {
        byteBufferFacade.flip ();
        while (byteBufferFacade.readComplete ()) {
            T message = hummingbirdDecoder.decoder (byteBufferFacade);
            if (message == null) {
                break;
            }
            hummingbirdHandler.channelMessage (socketChannelFacade, message);
        }
        byteBufferFacade.reclaim ();
    }

    private static <T> void close(SocketChannelFacade<T> socketChannelFacade, ByteBufferFacade byteBufferFacade) {
        socketChannelFacade.close ();
        byteBufferFacade.clear ();
    }
}
