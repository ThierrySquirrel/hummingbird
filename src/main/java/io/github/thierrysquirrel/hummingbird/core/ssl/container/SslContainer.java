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

package io.github.thierrysquirrel.hummingbird.core.ssl.container;

import io.github.thierrysquirrel.hummingbird.core.factory.SocketAddressFactory;
import io.github.thierrysquirrel.hummingbird.core.ssl.http.builder.SslHttpBuilder;
import io.github.thierrysquirrel.jellyfish.concurrency.map.hash.ConcurrencyHashMap;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: SslContainer
 * Description:
 * Date:2026/9/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class SslContainer {
    private SslContainer() {
    }

    private static final Logger logger = Logger.getLogger(SslContainer.class.getName());


    private static final ConcurrencyHashMap<String, SSLContext> SSL_CONTAINER_MAP = new ConcurrencyHashMap<>();

    public static void setSslContext(String ipAndPort, SSLContext sslContext) {
        SSL_CONTAINER_MAP.set(ipAndPort, sslContext);
    }

    public static void setHttpSslContext(String ipAndPort, String resourcesFileName, String password) {
        SSLContext sslContext = SslHttpBuilder.builderPkcsSslContext(resourcesFileName, password);
        setSslContext(ipAndPort, sslContext);
    }

    public static SSLContext getSslContext(SocketChannel socketChannel) {
        String ipAndPort = getIpAndPort(socketChannel);
        return SSL_CONTAINER_MAP.get(ipAndPort);
    }

    public static void deleteValue(SocketChannel socketChannel) {
        String ipAndPort = getIpAndPort(socketChannel);
        SSL_CONTAINER_MAP.deleteValue(ipAndPort);
    }

    public static boolean portOpenSsl(SocketChannel socketChannel) {
        String ipAndPort = getIpAndPort(socketChannel);
        SSLContext sslContext = SSL_CONTAINER_MAP.get(ipAndPort);
        if (sslContext == null) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    private static String getIpAndPort(SocketChannel socketChannel) {
        try {
            InetSocketAddress inetSocketAddress = ((InetSocketAddress) socketChannel.getLocalAddress());
            return SocketAddressFactory.getIpAndPort(inetSocketAddress);
        } catch (IOException e) {
            String logMsg = "socketChannel Close ,getLocalAddress Error";
            logger.log(Level.WARNING, logMsg);
        }
        return null;
    }
}
