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
package com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory;

import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.HttpCoderConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequest;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpResponse;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.builder.HttpRequestBuilder;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.builder.HttpResponseBuilder;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderKeyConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.factory.HttpHeaderFactory;
import com.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.SocketChannelFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.builder.ByteBufferFacadeBuilder;
import com.google.common.collect.Maps;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

/**
 * Classname: HttpDecoderFactory
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HttpDecoderFactory {

    private HttpDecoderFactory() {
    }

    public static HttpRequest readHttpRequest(ByteBufferFacade byteBufferFacade) {
        byteBufferFacade.make();
        String readData = readLine(byteBufferFacade);
        if (Objects.isNull(readData)) {
            byteBufferFacade.reset();
            return null;
        }
        String[] httpRequestSplit = readData.split(HttpCoderConstant.SPACE_STRING);
        return HttpRequestBuilder.builderHttpRequest(httpRequestSplit[0], httpRequestSplit[1], httpRequestSplit[2]);
    }

    public static HttpResponse readHttpResponse(ByteBufferFacade byteBufferFacade) {
        byteBufferFacade.make();
        String readData = readLine(byteBufferFacade);
        if (Objects.isNull(readData)) {
            byteBufferFacade.reset();
            return null;
        }
        String[] httpResponseSplit = readData.split(HttpCoderConstant.SPACE_STRING);
        return HttpResponseBuilder.builderHttpResponse(httpResponseSplit[0], httpResponseSplit[1], httpResponseSplit[2]);
    }


    public static HttpRequestContext readHttpHeaderHttpBody(ByteBufferFacade byteBufferFacade, SocketChannelFacade<HttpRequestContext> socketChannelFacade, HttpRequestContext httpRequestContext) {
        HttpRequestContext readHttpHeader = readHttpHeader(byteBufferFacade, httpRequestContext);
        if (Objects.isNull(readHttpHeader)) {
            return null;
        }
        return tryReadHttpBody(byteBufferFacade, socketChannelFacade, httpRequestContext);
    }

    public static HttpRequestContext tryReadHttpBody(ByteBufferFacade byteBufferFacade, SocketChannelFacade<HttpRequestContext> socketChannelFacade, HttpRequestContext httpRequestContext) {
        String bodyLengthString = HttpHeaderFactory.getHttpHeaderValue(httpRequestContext.getHttpHeader(), HttpHeaderKeyConstant.CONTENT_LENGTH);
        if (Objects.isNull(bodyLengthString)) {
            return httpRequestContext;
        }
        ByteBuffer httpBody = httpRequestContext.getHttpBody();
        int bodyLength = Integer.parseInt(bodyLengthString);
        if (Objects.isNull(httpBody)) {
            httpBody = ByteBuffer.allocateDirect(bodyLength);
            httpRequestContext.setHttpBody(httpBody);
        }
        boolean tryGet = byteBufferFacade.tryGet(httpBody);
        if (tryGet) {
            httpBody.flip();
            socketChannelFacade.removeMessageDecoderCache();
            return httpRequestContext;
        }
        socketChannelFacade.putMessageDecoderCache(httpRequestContext);
        return null;
    }

    private static HttpRequestContext readHttpHeader(ByteBufferFacade byteBufferFacade, HttpRequestContext httpRequestContext) {
        Map<String, String> readHttpHeader = Maps.newConcurrentMap();
        while (true) {
            String readData = readLine(byteBufferFacade);
            if (Objects.isNull(readData)) {
                byteBufferFacade.reset();
                return null;
            }
            if (readData.isEmpty()) {
                break;
            }
            String[] httpHeaderSplit = readData.split(HttpCoderConstant.COLON_STRING);
            readHttpHeader.put(httpHeaderSplit[0], httpHeaderSplit[1].strip());
        }
        httpRequestContext.setHttpHeader(readHttpHeader);
        return httpRequestContext;
    }

    public static String readLine(ByteBufferFacade byteBufferFacade) {
        ByteBufferFacade readData = ByteBufferFacadeBuilder.builderByteBufferFacade();
        boolean readSuccessful = Boolean.FALSE;
        while (byteBufferFacade.readComplete()) {
            byte data = byteBufferFacade.getByte();
            if (!byteBufferFacade.readComplete()) {
                return null;
            }
            if (data == HttpCoderConstant.CARRIAGE_RETURN) {
                byte nextData = byteBufferFacade.getByte();
                if (nextData == HttpCoderConstant.LINE_FEED) {
                    readSuccessful = Boolean.TRUE;
                    break;
                } else {
                    return null;
                }
            }
            readData.putByte(data);
        }
        if (!readSuccessful) {
            return null;
        }
        readData.flip();
        return new String(readData.getAllBytes());
    }
}
