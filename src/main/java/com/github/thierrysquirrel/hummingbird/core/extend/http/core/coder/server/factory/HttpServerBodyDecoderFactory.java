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
package com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.server.factory;

import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.HttpCoderConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.HttpFormDataCoderConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory.HttpDecoderFactory;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory.UrlCoderFactory;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.server.factory.constant.HttpServerBodyDecoderFactoryConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.server.factory.domain.HttpFormData;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.server.factory.domain.builder.HttpFormDataBuilder;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.factory.HttpHeaderFactory;
import com.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import com.github.thierrysquirrel.hummingbird.core.facade.builder.ByteBufferFacadeBuilder;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Classname: HttpServerBodyDecoderFactory
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
@Slf4j
public class HttpServerBodyDecoderFactory {
    private HttpServerBodyDecoderFactory() {
    }

    public static Map<String, HttpFormData> builderFormData(HttpRequestContext httpRequestContext) {
        String boundary = HttpHeaderFactory.getBoundary(httpRequestContext.getHttpHeader());
        Map<String, HttpFormData> formDataMap = Maps.newConcurrentMap();
        if (boundary == null) {
            return formDataMap;
        }
        ByteBufferFacade byteBufferFacade = getByteBufferFacade(httpRequestContext);
        String beginBoundary = HttpFormDataCoderConstant.DOUBLE_HYPHEN + boundary;
        String endBoundary = beginBoundary + HttpFormDataCoderConstant.DOUBLE_HYPHEN;

        whileReadFormData(byteBufferFacade, formDataMap, beginBoundary, endBoundary);
        return formDataMap;
    }

    public static Map<String, String> builderFormUrlencoded(HttpRequestContext httpRequestContext) {
        String bodyText = builderText(httpRequestContext);
        return UrlCoderFactory.builderUrlMap(bodyText);
    }

    public static String builderText(HttpRequestContext httpRequestContext) {
        return new String(builderBytes(httpRequestContext));
    }

    public static byte[] builderBytes(HttpRequestContext httpRequestContext) {
        return getByteBufferFacade(httpRequestContext).getAllBytes();
    }

    private static ByteBufferFacade getByteBufferFacade(HttpRequestContext httpRequestContext) {
        ByteBuffer httpBody = httpRequestContext.getHttpBody();
        return ByteBufferFacadeBuilder.builderByteBufferFacade(httpBody);
    }

    private static void whileReadFormData(ByteBufferFacade byteBufferFacade, Map<String, HttpFormData> formDataMap, String beginBoundary, String endBoundary) {
        while (byteBufferFacade.readComplete()) {
            String readBoundary = HttpDecoderFactory.readLine(byteBufferFacade);
            if (beginBoundary.equals(readBoundary)) {
                putFormDataMap(byteBufferFacade, formDataMap, beginBoundary, endBoundary);
            }
            if (endBoundary.equals(readBoundary)) {
                break;
            }
        }
    }

    private static void putFormDataMap(ByteBufferFacade byteBufferFacade, Map<String, HttpFormData> formDataMap, String beginBoundary, String endBoundary) {
        String contentDisposition = HttpDecoderFactory.readLine(byteBufferFacade);
        if (contentDisposition == null) {
            log.error("contentDisposition Error");
            return;
        }
        String[] splitContentDisposition = contentDisposition.split(HttpFormDataCoderConstant.SEMICOLON_STRING);
        String name = splitContentDisposition[1].strip();
        String key = getFormDataBodyNameValue(name);
        if (splitContentDisposition.length > HttpServerBodyDecoderFactoryConstant.TWO) {
            String fileName = splitContentDisposition[2].strip();
            String fileNameValue = getFormDataBodyNameValue(fileName);

            String fileContentType = HttpDecoderFactory.readLine(byteBufferFacade);
            if (fileContentType == null) {
                log.error("fileContentType Error");
                return;
            }
            String[] splitContentType = fileContentType.split(HttpFormDataCoderConstant.COLON);
            String fileContentTypeValue = splitContentType[1].strip();

            ByteBuffer fileValue = readFormDataBodyFileBytes(byteBufferFacade, beginBoundary, endBoundary);
            formDataMap.put(key, HttpFormDataBuilder.builderFileHttpFormData(fileNameValue, fileContentTypeValue, fileValue));
        } else {
            String value = readFormDataBodyText(byteBufferFacade);
            formDataMap.put(key, HttpFormDataBuilder.builderHttpFormData(value));
        }
    }

    private static ByteBuffer readFormDataBodyFileBytes(ByteBufferFacade byteBufferFacade, String beginBoundary, String endBoundary) {
        skipBlankLines(byteBufferFacade);
        byteBufferFacade.make();
        int length = byteBufferFacade.length();
        int readLength = 0;
        boolean isRead = Boolean.TRUE;
        while (isRead) {
            int beginLength = byteBufferFacade.length();
            String readBoundary = HttpDecoderFactory.readLine(byteBufferFacade);
            if (readBoundary == null) {
                continue;
            }
            if (readBoundary.equals(beginBoundary) || readBoundary.equals(endBoundary)) {
                readLength = length - beginLength;
                byteBufferFacade.reset();
                isRead = Boolean.FALSE;
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(readLength);
        byteBufferFacade.getByteBuffer(byteBuffer);
        byteBuffer.flip();
        return byteBuffer;
    }

    private static String readFormDataBodyText(ByteBufferFacade byteBufferFacade) {
        skipBlankLines(byteBufferFacade);
        return HttpDecoderFactory.readLine(byteBufferFacade);
    }


    private static void skipBlankLines(ByteBufferFacade byteBufferFacade) {
        byteBufferFacade.make();
        if (byteBufferFacade.length() < HttpServerBodyDecoderFactoryConstant.TWO) {
            return;
        }
        byte data = byteBufferFacade.getByte();

        if (data == HttpCoderConstant.CARRIAGE_RETURN) {
            byte nextData = byteBufferFacade.getByte();
            if (nextData != HttpCoderConstant.LINE_FEED) {
                byteBufferFacade.reset();
            }
        } else {
            byteBufferFacade.reset();
        }
    }

    private static String getFormDataBodyNameValue(String name) {
        int nameValueIndex = name.indexOf(HttpFormDataCoderConstant.QUOTATION_MARK);
        return name.substring(nameValueIndex + 1, name.length() - 1);
    }

}
