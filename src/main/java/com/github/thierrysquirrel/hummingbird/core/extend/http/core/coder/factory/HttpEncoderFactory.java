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
package com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory;

import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.HttpCoderConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequest;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpResponse;
import com.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

/**
 * Classname: HttpEncoderFactory
 * Description:
 * Date: 2021/9/10 18:57
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class HttpEncoderFactory {
    private HttpEncoderFactory() {
    }

    public static void encoderHttpResponse(ByteBufferFacade byteBufferFacade, HttpRequestContext httpRequestContext) {
        HttpResponse httpResponse = httpRequestContext.getHttpResponse ();
        encoderHttpLine (byteBufferFacade, httpResponse.getHttpEdition ().getBytes (), httpResponse.getHttpStatusCode ().getBytes (), httpResponse.getHttpStatus ().getBytes ());
    }

    public static void encoderHttpRequest(ByteBufferFacade byteBufferFacade, HttpRequestContext httpRequestContext) {
        HttpRequest httpRequest = httpRequestContext.getHttpRequest ();
        encoderHttpLine (byteBufferFacade, httpRequest.getHttpMethod ().getBytes (), httpRequest.getHttpUri ().getBytes (), httpRequest.getHttpEdition ().getBytes ());
    }

    public static void encoderHttpHeaderAndHttpBody(ByteBufferFacade byteBufferFacade, HttpRequestContext httpRequestContext) {
        encoderHttpHeader (byteBufferFacade, httpRequestContext);
        encoderHttpBody (byteBufferFacade, httpRequestContext);
    }

    private static void encoderHttpHeader(ByteBufferFacade byteBufferFacade, HttpRequestContext httpRequestContext) {
        Map<String, String> httpHeader = httpRequestContext.getHttpHeader ();
        for (Map.Entry<String, String> httpHeaderEntry : httpHeader.entrySet ()) {
            String httpHeaderKey = httpHeaderEntry.getKey ();
            byteBufferFacade.putBytes (httpHeaderKey.getBytes ());
            byteBufferFacade.putByte (HttpCoderConstant.COLON);
            byteBufferFacade.putByte (HttpCoderConstant.SPACE);

            String httpHeaderValue = httpHeaderEntry.getValue ();
            byteBufferFacade.putBytes (httpHeaderValue.getBytes ());
            encoderHttpTag (byteBufferFacade);
        }
        encoderHttpTag (byteBufferFacade);
    }

    private static void encoderHttpBody(ByteBufferFacade byteBufferFacade, HttpRequestContext httpRequestContext) {
        ByteBuffer httpBody = httpRequestContext.getHttpBody ();
        if (!Objects.isNull (httpBody)) {
            byteBufferFacade.put (httpBody);
        }
    }

    private static void encoderHttpLine(ByteBufferFacade byteBufferFacade, byte[] data0, byte[] data1, byte[] data2) {
        encoderHttpLine (byteBufferFacade, data0);
        encoderHttpLine (byteBufferFacade, data1);
        encoderHttpLine (byteBufferFacade, data2);
        encoderHttpTag (byteBufferFacade);
    }

    private static void encoderHttpLine(ByteBufferFacade byteBufferFacade, byte[] data) {
        byteBufferFacade.putBytes (data);
        byteBufferFacade.putByte ((byte) 32);
    }

    private static void encoderHttpTag(ByteBufferFacade byteBufferFacade) {
        byteBufferFacade.putByte (HttpCoderConstant.CARRIAGE_RETURN);
        byteBufferFacade.putByte (HttpCoderConstant.LINE_FEED);
    }
}
