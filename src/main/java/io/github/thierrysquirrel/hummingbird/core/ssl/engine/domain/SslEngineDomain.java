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

package io.github.thierrysquirrel.hummingbird.core.ssl.engine.domain;

import javax.net.ssl.SSLEngine;
import java.nio.ByteBuffer;

/**
 * Classname: SslEngineDomain
 * Description:
 * Date:2026/9/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class SslEngineDomain {
    private SSLEngine sslEngine;
    private ByteBuffer socketEncryptRead;

    public SslEngineDomain(SSLEngine sslEngine, ByteBuffer socketEncryptRead) {
        this.sslEngine = sslEngine;
        this.socketEncryptRead = socketEncryptRead;
    }

    public SSLEngine getSslEngine() {
        return sslEngine;
    }

    public void setSslEngine(SSLEngine sslEngine) {
        this.sslEngine = sslEngine;
    }

    public ByteBuffer getSocketEncryptRead() {
        return socketEncryptRead;
    }

    public void setSocketEncryptRead(ByteBuffer socketEncryptRead) {
        this.socketEncryptRead = socketEncryptRead;
    }

    @Override
    public String toString() {
        return "SslEngineDomain{" +
                "sslEngine=" + sslEngine +
                ", socketEncryptRead=" + socketEncryptRead +
                '}';
    }
}
