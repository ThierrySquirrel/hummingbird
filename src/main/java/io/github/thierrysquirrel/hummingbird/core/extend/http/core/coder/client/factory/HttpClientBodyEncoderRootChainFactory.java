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
package io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.client.factory;

import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.client.factory.chain.FormDataChain;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.client.factory.chain.FormUrlencodedChain;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.HttpFormDataCoderConstant;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderKeyConstant;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderValueConstant;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.factory.HttpHeaderFactory;
import io.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import io.github.thierrysquirrel.hummingbird.core.facade.builder.ByteBufferFacadeBuilder;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Classname: HttpClientBodyEncoderRootChainFactory
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HttpClientBodyEncoderRootChainFactory {
    private HttpClientBodyEncoderRootChainFactory() {
    }

    public static FormUrlencodedChain createFormUrlencoded(HttpRequestContext httpRequestContext, String key, String value) {
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.FORM_URLENCODED);
        ByteBufferFacade httpBody = ByteBufferFacadeBuilder.builderDirectByteBufferFacade();
        FormUrlencodedFactory.putText(httpBody, key, value);
        return new FormUrlencodedChain(httpBody, httpRequestContext);
    }

    public static FormDataChain createFormData(HttpRequestContext httpRequestContext) {
        String boundary = HttpHeaderFactory.createBoundary();
        String formDataContentTypeValue = HttpHeaderFactory.createFormDataContentTypeValue(boundary);
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, formDataContentTypeValue);
        String beginBoundary = HttpFormDataCoderConstant.DOUBLE_HYPHEN + boundary;
        String engBoundary = beginBoundary + HttpFormDataCoderConstant.DOUBLE_HYPHEN;
        return new FormDataChain(httpRequestContext, beginBoundary, engBoundary);
    }

    public static void createJson(HttpRequestContext httpRequestContext, String body) {
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.JSON);
        putBytesBody(httpRequestContext, body.getBytes());
    }

    public static void createText(HttpRequestContext httpRequestContext, String body) {
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.TEXT_PLAIN);
        putBytesBody(httpRequestContext, body.getBytes());
    }

    public static void createOctetStream(HttpRequestContext httpRequestContext, ByteBuffer body) {
        Map<String, String> httpHeader = httpRequestContext.getHttpHeader();
        httpHeader.put(HttpHeaderKeyConstant.CONTENT_TYPE, HttpHeaderValueConstant.OCTET_STREAM);
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_LENGTH, body.limit() + "");
        httpRequestContext.setHttpBody(body);

    }

    private static void putBytesBody(HttpRequestContext httpRequestContext, byte[] bodyBytes) {
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_LENGTH, bodyBytes.length + "");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bodyBytes.length);
        byteBuffer.put(bodyBytes);
        byteBuffer.flip();
        httpRequestContext.setHttpBody(byteBuffer);
    }
}
