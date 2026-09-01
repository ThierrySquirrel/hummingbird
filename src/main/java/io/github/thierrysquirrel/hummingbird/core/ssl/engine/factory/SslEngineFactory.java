/**
 * Copyright 2026/9/1 ThierrySquirrel
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

package io.github.thierrysquirrel.hummingbird.core.ssl.engine.factory;

import io.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import io.github.thierrysquirrel.hummingbird.core.facade.builder.ByteBufferFacadeBuilder;
import io.github.thierrysquirrel.hummingbird.core.facade.cache.SocketChannelFacadeCache;
import io.github.thierrysquirrel.hummingbird.core.ssl.container.SslContainer;
import io.github.thierrysquirrel.hummingbird.core.ssl.engine.builder.SslEngineByteBufferBuilder;
import io.github.thierrysquirrel.hummingbird.core.ssl.engine.domain.SslEngineDomain;
import io.github.thierrysquirrel.hummingbird.core.ssl.engine.domain.container.SslEngineDomainContainer;
import io.github.thierrysquirrel.hummingbird.core.ssl.engine.factory.constant.SslEngineFactoryConstant;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: SslEngineFactory
 * Description:
 * Date:2026/9/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class SslEngineFactory {
    private SslEngineFactory() {
    }

    private static final Logger logger = Logger.getLogger(SslEngineFactory.class.getName());

    public static int sslIsReadable(SocketChannel socketChannel, ByteBufferFacade byteBufferFacade) {
        SslEngineFactory.sslHandshake(socketChannel);
        int readOffset = SslEngineFactory.readEncrypt(socketChannel);
        boolean putReadWhile = SslEngineFactory.putReadWhile(socketChannel, byteBufferFacade);
        if (!putReadWhile) {
            return SslEngineFactoryConstant.SSL_IS_READABLE_CLOSE;
        }
        return readOffset;
    }

    private static void sslHandshake(SocketChannel socketChannel) {
        SslEngineDomain sslEngineDomain = SslEngineDomainContainer.getSslEngineDomain(socketChannel);
        boolean handshakeWhile = true;
        while (handshakeWhile) {
            SSLEngineResult.HandshakeStatus status = sslEngineDomain.getSslEngine().getHandshakeStatus();
            switch (status) {
                case SSLEngineResult.HandshakeStatus.NEED_UNWRAP -> needUnwrap(socketChannel);
                case SSLEngineResult.HandshakeStatus.NEED_WRAP -> needWrap(socketChannel);
                case SSLEngineResult.HandshakeStatus.NEED_TASK -> needTask(socketChannel);
                case SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING -> handshakeWhile = false;
            }
        }
    }

    public static int sendSslMessage(SocketChannel socketChannel, ByteBufferFacade byteBufferFacade) throws IOException {
        SslEngineDomain sslEngineDomain = SslEngineDomainContainer.getSslEngineDomain(socketChannel);
        SSLEngine engine = sslEngineDomain.getSslEngine();

        ByteBufferFacade sslSocketWrite = ByteBufferFacadeBuilder.builderDirectByteBufferFacade();

        int bytesProduced = -1;
        while (bytesProduced != 0) {
            ByteBuffer byteBuffer = byteBufferFacade.getByteBuffer();
            ByteBuffer socketWrite = SslEngineByteBufferBuilder.builderDefaultDirect();
            SSLEngineResult wrap = wrap(engine, byteBuffer, socketWrite);

            socketWrite.flip();
            sslSocketWrite.put(socketWrite);
            socketWrite.clear();

            bytesProduced = wrap.bytesProduced();
        }

        sslSocketWrite.flip();
        int write = -1;
        while (sslSocketWrite.length() != 0) {
            write = socketChannel.write(sslSocketWrite.getByteBuffer());
        }
        sslSocketWrite.clear();
        return write;
    }

    public static void closeSsl(SocketChannel socketChannel) {

        boolean openSsl = SslContainer.portOpenSsl(socketChannel);
        if (!openSsl) {
            return;
        }

        SslEngineDomain sslEngineDomain = SslEngineDomainContainer.getSslEngineDomain(socketChannel);
        SSLEngine engine = sslEngineDomain.getSslEngine();

        engine.closeOutbound();
        ByteBuffer socketWrite = SslEngineByteBufferBuilder.builderDefaultDirect();

        SSLEngineResult result = wrap(engine, ByteBuffer.allocateDirect(0), socketWrite);

        socketWrite.flip();
        if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
            socketChannelWrite(socketChannel, socketWrite);
        }

        socketWrite.clear();
        SslEngineDomainContainer.deleteValue(socketChannel);
    }

    private static int readEncrypt(SocketChannel socketChannel) {
        SslEngineDomain sslEngineDomain = SslEngineDomainContainer.getSslEngineDomain(socketChannel);
        ByteBuffer socketEncryptRead = sslEngineDomain.getSocketEncryptRead();

        return socketChannelRead(socketChannel, socketEncryptRead);
    }

    private static boolean putReadWhile(SocketChannel socketChannel, ByteBufferFacade byteBufferFacade) {
        SslEngineDomain sslEngineDomain = SslEngineDomainContainer.getSslEngineDomain(socketChannel);
        ByteBuffer socketEncryptRead = sslEngineDomain.getSocketEncryptRead();
        int bytesProduced = -1;
        while (bytesProduced != 0) {
            SSLEngineResult sslEngineResult = putRead(socketChannel, byteBufferFacade);
            bytesProduced = sslEngineResult.bytesProduced();

            if (sslEngineResult.getStatus() == SSLEngineResult.Status.CLOSED) {
                SocketChannelFacadeCache.get(socketChannel.toString()).close();
                return false;
            }
        }
        socketEncryptRead.compact();
        return true;
    }

    private static SSLEngineResult putRead(SocketChannel socketChannel, ByteBufferFacade byteBufferFacade) {
        SslEngineDomain sslEngineDomain = SslEngineDomainContainer.getSslEngineDomain(socketChannel);
        SSLEngine engine = sslEngineDomain.getSslEngine();
        ByteBuffer socketEncryptRead = sslEngineDomain.getSocketEncryptRead();

        ByteBuffer socketRead = SslEngineByteBufferBuilder.builderDefaultDirect();

        SSLEngineResult unwrap = unwrap(engine, socketEncryptRead, socketRead, socketChannel.toString());

        socketRead.flip();
        byteBufferFacade.put(socketRead);
        socketRead.clear();

        return unwrap;
    }

    private static void needUnwrap(SocketChannel socketChannel) {
        SslEngineDomain sslEngineDomain = SslEngineDomainContainer.getSslEngineDomain(socketChannel);
        ByteBuffer socketEncryptRead = sslEngineDomain.getSocketEncryptRead();

        socketChannelRead(socketChannel, socketEncryptRead);

        SSLEngine engine = sslEngineDomain.getSslEngine();
        unwrap(engine, socketEncryptRead, ByteBuffer.allocateDirect(0), socketChannel.toString());
        socketEncryptRead.compact();
    }

    private static void needWrap(SocketChannel socketChannel) {
        SslEngineDomain sslEngineDomain = SslEngineDomainContainer.getSslEngineDomain(socketChannel);
        ByteBuffer socketEncryptWrit = SslEngineByteBufferBuilder.builderDefaultDirect();

        SSLEngine engine = sslEngineDomain.getSslEngine();
        wrap(engine, ByteBuffer.allocateDirect(0), socketEncryptWrit);

        socketEncryptWrit.flip();
        socketChannelWrite(socketChannel, socketEncryptWrit);
    }

    private static void needTask(SocketChannel socketChannel) {
        SslEngineDomain sslEngineDomain = SslEngineDomainContainer.getSslEngineDomain(socketChannel);
        SSLEngine engine = sslEngineDomain.getSslEngine();
        engine.getDelegatedTask().run();
    }


    private static SSLEngineResult unwrap(SSLEngine engine, ByteBuffer src,
                                          ByteBuffer dst, String channelString) {
        SSLEngineResult unwrap = null;
        try {
            unwrap = engine.unwrap(src, dst);
        } catch (SSLException e) {
            SocketChannelFacadeCache.get(channelString).close();
            String logMsg = "unwrap Error";
            logger.log(Level.WARNING, logMsg, e);
        }
        return unwrap;
    }

    private static SSLEngineResult wrap(SSLEngine engine, ByteBuffer src,
                                        ByteBuffer dst) {
        SSLEngineResult wrap = null;
        try {
            wrap = engine.wrap(src, dst);
        } catch (SSLException e) {
            String logMsg = "wrap Error";
            logger.log(Level.WARNING, logMsg, e);
        }
        return wrap;
    }

    private static int socketChannelRead(SocketChannel socketChannel, ByteBuffer byteBuffer) {
        int read = 0;
        try {
            read = socketChannel.read(byteBuffer);
            byteBuffer.flip();
        } catch (IOException e) {
            String logMsg = "socketChannelRead Error";
            logger.log(Level.WARNING, logMsg, e);
        }
        return read;
    }

    private static void socketChannelWrite(SocketChannel socketChannel, ByteBuffer byteBuffer) {
        try {
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        } catch (IOException e) {
            String logMsg = "socketChannelWrite Error";
            logger.log(Level.WARNING, logMsg, e);
        }
    }

}
