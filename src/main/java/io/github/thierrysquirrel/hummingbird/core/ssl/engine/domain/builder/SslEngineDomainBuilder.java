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

package io.github.thierrysquirrel.hummingbird.core.ssl.engine.domain.builder;

import io.github.thierrysquirrel.hummingbird.core.ssl.container.SslContainer;
import io.github.thierrysquirrel.hummingbird.core.ssl.engine.builder.SslEngineByteBufferBuilder;
import io.github.thierrysquirrel.hummingbird.core.ssl.engine.domain.SslEngineDomain;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: SslEngineDomainBuilder
 * Description:
 * Date:2026/9/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class SslEngineDomainBuilder {
    private SslEngineDomainBuilder() {
    }

    private static final Logger logger = Logger.getLogger(SslEngineDomainBuilder.class.getName());


    public static SslEngineDomain builderHttpSslEngineDomain(SocketChannel socketChannel) {
        SSLContext sslContext = SslContainer.getSslContext(socketChannel);
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);
        try {
            sslEngine.beginHandshake();
        } catch (SSLException e) {
            String logMsg = "beginHandshake Error";
            logger.log(Level.WARNING, logMsg, e);
        }

        ByteBuffer socketEncryptRead = SslEngineByteBufferBuilder.builderDefaultDirect();
        return new SslEngineDomain(sslEngine, socketEncryptRead);
    }
}
