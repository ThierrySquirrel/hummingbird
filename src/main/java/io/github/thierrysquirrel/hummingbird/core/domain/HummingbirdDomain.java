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
package io.github.thierrysquirrel.hummingbird.core.domain;

import io.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import io.github.thierrysquirrel.hummingbird.core.coder.HummingbirdEncoder;
import io.github.thierrysquirrel.hummingbird.core.coder.container.HummingbirdDecoderCache;
import io.github.thierrysquirrel.hummingbird.core.domain.cache.ChannelHeartbeatDomainCache;
import io.github.thierrysquirrel.hummingbird.core.handler.HummingbirdHandler;

/**
 * Classname: HummingbirdDomain
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HummingbirdDomain<T> {
    private HummingbirdDecoder<T> hummingbirdDecoder;
    private HummingbirdEncoder<T> hummingbirdEncoder;
    private HummingbirdHandler<T> hummingbirdHandler;
    private ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache;
    private HummingbirdDecoderCache<T> hummingbirdDecoderCache;

    public HummingbirdDecoder<T> getHummingbirdDecoder() {
        return hummingbirdDecoder;
    }

    public void setHummingbirdDecoder(HummingbirdDecoder<T> hummingbirdDecoder) {
        this.hummingbirdDecoder = hummingbirdDecoder;
    }

    public HummingbirdEncoder<T> getHummingbirdEncoder() {
        return hummingbirdEncoder;
    }

    public void setHummingbirdEncoder(HummingbirdEncoder<T> hummingbirdEncoder) {
        this.hummingbirdEncoder = hummingbirdEncoder;
    }

    public HummingbirdHandler<T> getHummingbirdHandler() {
        return hummingbirdHandler;
    }

    public void setHummingbirdHandler(HummingbirdHandler<T> hummingbirdHandler) {
        this.hummingbirdHandler = hummingbirdHandler;
    }

    public ChannelHeartbeatDomainCache<T> getChannelHeartbeatDomainCache() {
        return channelHeartbeatDomainCache;
    }

    public void setChannelHeartbeatDomainCache(ChannelHeartbeatDomainCache<T> channelHeartbeatDomainCache) {
        this.channelHeartbeatDomainCache = channelHeartbeatDomainCache;
    }

    public HummingbirdDecoderCache<T> getHummingbirdDecoderCache() {
        return hummingbirdDecoderCache;
    }

    public void setHummingbirdDecoderCache(HummingbirdDecoderCache<T> hummingbirdDecoderCache) {
        this.hummingbirdDecoderCache = hummingbirdDecoderCache;
    }

    @Override
    public String toString() {
        return "HummingbirdDomain{" +
                "hummingbirdDecoder=" + hummingbirdDecoder +
                ", hummingbirdEncoder=" + hummingbirdEncoder +
                ", hummingbirdHandler=" + hummingbirdHandler +
                ", channelHeartbeatDomainCache=" + channelHeartbeatDomainCache +
                ", hummingbirdDecoderCache=" + hummingbirdDecoderCache +
                '}';
    }
}
