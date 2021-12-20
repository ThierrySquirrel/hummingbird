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
package com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.builder;

import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequest;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpResponse;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpEditionConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderKeyConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderValueConstant;
import com.google.common.collect.Maps;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Classname: HttpRequestContextBuilder
 * Description:
 * Date: 2021/9/10 18:31
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class HttpRequestContextBuilder {
    private HttpRequestContextBuilder() {
    }

    public static HttpRequestContext builderHttpServerDecoder(HttpRequest httpRequest) {
        HttpRequestContext httpRequestContext = new HttpRequestContext ();
        httpRequestContext.setHttpRequest (httpRequest);
        return httpRequestContext;
    }

    public static HttpRequestContext builderHttpClientDecoder(HttpResponse httpResponse) {
        HttpRequestContext httpRequestContext = new HttpRequestContext ();
        httpRequestContext.setHttpResponse (httpResponse);
        return httpRequestContext;
    }

    public static HttpRequestContext builderRequest(String httpMethod, String httpUri) {
        HttpRequestContext httpRequestContext = new HttpRequestContext ();
        builderDefaultHttpHeader (httpRequestContext);
        HttpRequest httpRequest = HttpRequestBuilder.builderHttpRequest (httpMethod, httpUri, HttpEditionConstant.DEFAULT_EDITION);
        httpRequestContext.setHttpRequest (httpRequest);
        return httpRequestContext;
    }

    public static HttpRequestContext builderTextResponse(String body) {
        HttpRequestContext httpRequestContext = new HttpRequestContext ();
        builderResponseBody (httpRequestContext, body);
        httpRequestContext.getHttpHeader ().put (HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.TEXT_PLAIN);

        HttpResponse httpResponse = HttpResponseBuilder.builderDefault ();
        httpRequestContext.setHttpResponse (httpResponse);
        return httpRequestContext;
    }

    public static HttpRequestContext builderJsonResponse(String body) {
        HttpRequestContext httpRequestContext = new HttpRequestContext ();
        builderResponseBody (httpRequestContext, body);
        httpRequestContext.getHttpHeader ().put (HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.JSON);

        return httpRequestContext;
    }

    private static void builderResponseBody(HttpRequestContext httpRequestContext, String body) {
        builderDefaultHttpHeader (httpRequestContext);
        byte[] bodyBytes = body.getBytes ();
        int bodyLength = bodyBytes.length;
        httpRequestContext.getHttpHeader ().put (HttpHeaderKeyConstant.CONTENT_LENGTH, bodyLength + "");

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect (bodyLength);
        byteBuffer.put (bodyBytes);
        byteBuffer.flip ();
        httpRequestContext.setHttpBody (byteBuffer);
    }

    private static void builderDefaultHttpHeader(HttpRequestContext httpRequestContext) {
        Map<String, String> httpHeader = Maps.newConcurrentMap ();
        httpHeader.put (HttpHeaderKeyConstant.CONNECTION, HttpHeaderValueConstant.KEEP_ALIVE);
        httpRequestContext.setHttpHeader (httpHeader);
    }
}
