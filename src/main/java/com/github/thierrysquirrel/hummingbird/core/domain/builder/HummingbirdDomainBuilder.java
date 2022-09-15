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
package com.github.thierrysquirrel.hummingbird.core.domain.builder;

import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import com.github.thierrysquirrel.hummingbird.core.coder.container.HummingbirdDecoderCache;
import com.github.thierrysquirrel.hummingbird.core.domain.HummingbirdDomain;
import com.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import com.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;

/**
 * Classname: HummingbirdDomainBuilder
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HummingbirdDomainBuilder {
    private HummingbirdDomainBuilder() {
    }

    public static <T> HummingbirdDomain<T> builderHummingbirdDomain(HummingbirdDecoder<T> hummingbirdDecoder, HummingbirdEncoder<T> hummingbirdEncoder, HummingbirdHandler<T> hummingbirdHandler, ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache, HummingbirdDecoderCache<T> hummingbirdDecoderCache) {
        HummingbirdDomain<T> hummingbirdDomain = new HummingbirdDomain<>();
        hummingbirdDomain.setHummingbirdDecoder(hummingbirdDecoder);
        hummingbirdDomain.setHummingbirdEncoder(hummingbirdEncoder);
        hummingbirdDomain.setHummingbirdHandler(hummingbirdHandler);
        hummingbirdDomain.setChannelHeartbeatDomainCache(channelHeartbeatDomainCache);
        hummingbirdDomain.setHummingbirdDecoderCache(hummingbirdDecoderCache);
        return hummingbirdDomain;
    }
}
