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

package io.github.thierrysquirrel.hummingbird.core.ssl.http.builder;

import io.github.thierrysquirrel.hummingbird.core.ssl.http.builder.constant.SslHttpBuilderConstant;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classname: SslHttpBuilder
 * Description:
 * Date:2026/9/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class SslHttpBuilder {
    private SslHttpBuilder() {
    }

    private static final Logger logger = Logger.getLogger(SslHttpBuilder.class.getName());


    public static SSLContext builderPkcsSslContext(String resourcesFileName, String password) {

        SSLContext sslContext = null;
        char[] passwordChars = password.toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance(SslHttpBuilderConstant.PKCS12);

            InputStream keyStoreStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcesFileName);
            keyStore.load(keyStoreStream, passwordChars);
            keyStoreStream.close();

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, passwordChars);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            sslContext = SSLContext.getInstance(SslHttpBuilderConstant.TLS);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new java.security.SecureRandom());
        } catch (Exception e) {
            String logMsg = "builderPkcsSslContext Error";
            logger.log(Level.WARNING, logMsg, e);
        }

        return sslContext;
    }
}
