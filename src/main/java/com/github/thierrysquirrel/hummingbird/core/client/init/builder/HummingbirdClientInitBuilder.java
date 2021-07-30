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
package com.github.thierrysquirrel.hummingbird.core.client.init.builder;

import com.github.thierrysquirrel.hummingbird.core.client.domain.HummingbirdClientDomain;
import com.github.thierrysquirrel.hummingbird.core.client.domain.builder.HummingbirdClientDomainBuilder;
import com.github.thierrysquirrel.hummingbird.core.client.init.HummingbirdClientInit;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Classname: HummingbirdClientInitBuilder
 * Description:
 * Date: 2021/7/30 13:28
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class HummingbirdClientInitBuilder {
    private HummingbirdClientInitBuilder() {
    }

    public static <T> HummingbirdClientInit<T> builderHummingbirdClientInit(ThreadPoolExecutor hummingbirdClientThreadPool, String url, long readHeartbeatTime, long writeHeartbeatTime, HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler) {
        HummingbirdClientDomain<T> hummingbirdClientDomain = HummingbirdClientDomainBuilder.builderHummingbirdClientDomain (hummingbirdClientThreadPool, url, readHeartbeatTime, writeHeartbeatTime, hummingbirdDecoder, hummingbirdEncoder, hummingbirdHandler);
        HummingbirdClientInit<T> hummingbirdClientInit = new HummingbirdClientInit<> ();
        hummingbirdClientInit.setHummingbirdClientDomain (hummingbirdClientDomain);
        return hummingbirdClientInit;
    }
}
