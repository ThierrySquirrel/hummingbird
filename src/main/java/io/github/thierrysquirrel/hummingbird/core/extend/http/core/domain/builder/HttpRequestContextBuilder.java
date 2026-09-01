/**
 * Copyright 2026/6/1 ThierrySquirrel
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
package io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.builder;

import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequest;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpResponse;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpEditionConstant;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderKeyConstant;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderValueConstant;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Classname: HttpRequestContextBuilder
 * Description:
 * Date:2026/6/1
 *
 * @author ThierrySquirrel
 * @since JDK25
 **/
public class HttpRequestContextBuilder {
    private HttpRequestContextBuilder() {
    }

    public static HttpRequestContext builderHttpServerDecoder(HttpRequest httpRequest) {
        HttpRequestContext httpRequestContext = new HttpRequestContext();
        httpRequestContext.setHttpRequest(httpRequest);
        return httpRequestContext;
    }

    public static HttpRequestContext builderHttpClientDecoder(HttpResponse httpResponse) {
        HttpRequestContext httpRequestContext = new HttpRequestContext();
        httpRequestContext.setHttpResponse(httpResponse);
        return httpRequestContext;
    }

    public static HttpRequestContext builderRequest(String httpMethod, String httpUri) {
        HttpRequestContext httpRequestContext = new HttpRequestContext();
        builderDefaultHttpHeader(httpRequestContext);
        HttpRequest httpRequest = HttpRequestBuilder.builderHttpRequest(httpMethod, httpUri, HttpEditionConstant.DEFAULT_EDITION);
        httpRequestContext.setHttpRequest(httpRequest);
        return httpRequestContext;
    }

    public static HttpRequestContext builderTextResponse(HttpRequestContext httpRequestContext, String body) {
        builderResponseBody(httpRequestContext, body);
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.TEXT_PLAIN);
        return httpRequestContext;
    }

    public static HttpRequestContext builderTextResponse(String body) {
        HttpRequestContext httpRequestContext = builderDefaultResponse();
        return builderTextResponse(httpRequestContext, body);
    }

    public static HttpRequestContext builderJsonResponse(HttpRequestContext httpRequestContext, String body) {
        builderResponseBody(httpRequestContext, body);
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.JSON);
        return httpRequestContext;
    }

    public static HttpRequestContext builderJsonResponse(String body) {
        HttpRequestContext httpRequestContext = builderDefaultResponse();
        return builderJsonResponse(httpRequestContext, body);
    }

    public static HttpRequestContext builderOctetStream(HttpRequestContext httpRequestContext, byte[] body) {
        builderResponseBody(httpRequestContext, body);
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.OCTET_STREAM);
        return httpRequestContext;
    }

    public static HttpRequestContext builderOctetStream(byte[] body) {
        HttpRequestContext httpRequestContext = builderDefaultResponse();
        return builderOctetStream(httpRequestContext, body);
    }

    public static HttpRequestContext builderFavicon(HttpRequestContext httpRequestContext, byte[] body) {
        builderResponseBody(httpRequestContext, body);
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.ICON);
        return httpRequestContext;
    }

    public static HttpRequestContext builderFavicon(byte[] body) {
        HttpRequestContext httpRequestContext = builderDefaultResponse();
        return builderFavicon(httpRequestContext, body);
    }

    public static HttpRequestContext builderTextHtmlUtf(HttpRequestContext httpRequestContext, byte[] body) {
        builderResponseBody(httpRequestContext, body);
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.TEXT_HTML_UTF);
        return httpRequestContext;
    }

    public static HttpRequestContext builderTextHtmlUtf(byte[] body) {
        HttpRequestContext httpRequestContext = builderDefaultResponse();
        return builderTextHtmlUtf(httpRequestContext, body);
    }

    public static HttpRequestContext builderJpeg(HttpRequestContext httpRequestContext, byte[] body) {
        builderResponseBody(httpRequestContext, body);
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.JPG);
        return httpRequestContext;
    }

    public static HttpRequestContext builderJpeg(byte[] body) {
        HttpRequestContext httpRequestContext = builderDefaultResponse();
        return builderJpeg(httpRequestContext, body);
    }

    public static HttpRequestContext builderDownload(HttpRequestContext httpRequestContext, byte[] body, String fileName) {
        builderResponseBody(httpRequestContext, body);
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.OCTET_STREAM);

        String attachment = String.format(HttpHeaderValueConstant.ATTACHMENT, fileName);
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_DISPOSITION, attachment);
        return httpRequestContext;
    }

    public static HttpRequestContext builderDownload(byte[] body, String fileName) {
        HttpRequestContext httpRequestContext = builderDefaultResponse();
        return builderDownload(httpRequestContext, body, fileName);
    }

    public static HttpRequestContext builderDefaultResponse() {
        HttpRequestContext httpRequestContext = new HttpRequestContext();
        builderDefaultHttpHeader(httpRequestContext);

        HttpResponse httpResponse = HttpResponseBuilder.builderDefault();
        httpRequestContext.setHttpResponse(httpResponse);
        return httpRequestContext;
    }

    private static void builderResponseBody(HttpRequestContext httpRequestContext, String body) {
        byte[] bodyBytes = body.getBytes();
        builderResponseBody(httpRequestContext, bodyBytes);
    }

    private static void builderResponseBody(HttpRequestContext httpRequestContext, byte[] body) {
        int bodyLength = body.length;
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_LENGTH, bodyLength + "");

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bodyLength);
        byteBuffer.put(body);
        byteBuffer.flip();

        httpRequestContext.setHttpBody(byteBuffer);
    }

    private static void builderDefaultHttpHeader(HttpRequestContext httpRequestContext) {
        Map<String, String> httpHeader = new HashMap<>();
        httpHeader.put(HttpHeaderKeyConstant.CONNECTION, HttpHeaderValueConstant.KEEP_ALIVE);
        httpRequestContext.setHttpHeader(httpHeader);
    }
}
