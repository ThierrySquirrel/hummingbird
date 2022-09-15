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
package com.github.thierrysquirrel.hummingbird.core.factory;

import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import com.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.builder.SocketChannelFacadeBuilder;
import com.github.thierrysquirrel.hummingbird.core.facade.cache.ByteBufferFacadeChannelReadCache;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;
import com.github.thierrysquirrel.hummingbird.core.server.factory.constant.ServerSocketSelectorKeysFactoryConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Classname: SocketSelectorKeysFactory
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
@Slf4j
public class SocketSelectorKeysFactory {
    private SocketSelectorKeysFactory() {
    }

    public static SelectionKey getSelectionKey(Iterator<SelectionKey> selectionKeyIterator) {
        SelectionKey selectionKey = selectionKeyIterator.next();
        selectionKeyIterator.remove();
        if (!selectionKey.isValid()) {
            return null;
        }
        return selectionKey;
    }

    public static <T> void isReadable(SocketChannel socketChannel, HummingbirdDomain<T> hummingbirdDomain) throws IOException {
        SocketChannelFacade<T> socketChannelFacade = SocketChannelFacadeBuilder.builderSocketChannelFacade(hummingbirdDomain.getHummingbirdEncoder(), hummingbirdDomain.getHummingbirdHandler(), hummingbirdDomain.getChannelHeartbeatDomainCache(), hummingbirdDomain.getHummingbirdDecoderCache(), socketChannel);

        int readOffsetInit = ServerSocketSelectorKeysFactoryConstant.READ_OFFSET_INIT;
        int readOffset = readOffsetInit;

        ByteBufferFacade byteBufferFacade = ByteBufferFacadeChannelReadCache.getByteBufferFacade(socketChannel.toString());
        while (readOffset == readOffsetInit || readOffset > 0) {
            try {
                hummingbirdDomain.getChannelHeartbeatDomainCache().readHeartbeat(socketChannelFacade);
                readOffset = socketChannel.read(byteBufferFacade.getByteBuffer());
                boolean expansion = byteBufferFacade.isExpansion();
                read(hummingbirdDomain.getHummingbirdDecoder(), hummingbirdDomain.getHummingbirdHandler(), socketChannelFacade, byteBufferFacade);
                byteBufferFacade.tryExpansion(expansion);
            } catch (IOException e) {
                log.error("read Error", e);
                break;
            }
        }
        if (readOffset < 0) {
            socketChannelFacade.close();
        }
    }

    private static <T> void read(HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdHandler<T> hummingbirdHandler, SocketChannelFacade<T> socketChannelFacade, ByteBufferFacade byteBufferFacade) {
        byteBufferFacade.flip();
        while (byteBufferFacade.readComplete()) {
            T message = hummingbirdDecoder.decoder(byteBufferFacade, socketChannelFacade);
            if (message == null) {
                break;
            }
            hummingbirdHandler.channelMessage(socketChannelFacade, message);
        }
    }

}
