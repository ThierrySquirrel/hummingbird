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
package io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.server;

import io.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory.HttpDecoderFactory;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequest;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.builder.HttpRequestContextBuilder;
import io.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import io.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;

import java.util.Objects;

/**
 * Classname: HttpServerDecoder
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HttpServerDecoder implements HummingbirdDecoder<HttpRequestContext> {
    @Override
    public HttpRequestContext decoder(ByteBufferFacade byteBufferFacade, SocketChannelFacade<HttpRequestContext> socketChannelFacade) {
        HttpRequestContext messageDecoderCache = socketChannelFacade.getMessageDecoderCache();
        if (!Objects.isNull(messageDecoderCache)) {
            return HttpDecoderFactory.tryReadHttpBody(byteBufferFacade, socketChannelFacade, messageDecoderCache);
        }

        HttpRequest httpRequest = HttpDecoderFactory.readHttpRequest(byteBufferFacade);
        if (Objects.isNull(httpRequest)) {
            return null;
        }
        HttpRequestContext httpRequestContext = HttpRequestContextBuilder.builderHttpServerDecoder(httpRequest);

        return HttpDecoderFactory.readHttpHeaderHttpBody(byteBufferFacade, socketChannelFacade, httpRequestContext);
    }
}
