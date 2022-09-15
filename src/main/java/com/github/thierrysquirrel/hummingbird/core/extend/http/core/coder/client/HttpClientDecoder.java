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
package com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.client;

import com.github.thierrysquirrel.hummingbird.core.coder.HummingbirdDecoder;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory.HttpDecoderFactory;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpResponse;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.builder.HttpRequestContextBuilder;
import com.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;

import java.util.Objects;

/**
 * Classname: HttpClientDecoder
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HttpClientDecoder implements HummingbirdDecoder<HttpRequestContext> {
    @Override
    public HttpRequestContext decoder(ByteBufferFacade byteBufferFacade, SocketChannelFacade<HttpRequestContext> socketChannelFacade) {
        HttpRequestContext messageDecoderCache = socketChannelFacade.getMessageDecoderCache();
        if (!Objects.isNull(messageDecoderCache)) {
            return HttpDecoderFactory.tryReadHttpBody(byteBufferFacade, socketChannelFacade, messageDecoderCache);
        }

        HttpResponse httpResponse = HttpDecoderFactory.readHttpResponse(byteBufferFacade);
        if (Objects.isNull(httpResponse)) {
            return null;
        }
        HttpRequestContext httpRequestContext = HttpRequestContextBuilder.builderHttpClientDecoder(httpResponse);

        return HttpDecoderFactory.readHttpHeaderHttpBody(byteBufferFacade, socketChannelFacade, httpRequestContext);
    }
}
