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
package com.github.thierrysquirrel.hummingbird.core.extend.http.core.factory;

import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderKeyConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderValueConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.factory.constant.BoundaryConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.factory.constant.HttpHeaderFactoryConstant;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Classname: HttpHeaderFactory
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HttpHeaderFactory {

    private HttpHeaderFactory() {
    }

    public static String getHttpHeaderValue(Map<String, String> httpHeader, String headerKey) {
        String headerValue = httpHeader.get(headerKey);
        if (!Objects.isNull(headerValue)) {
            return headerValue;
        }
        String headerKeyLowerCase = headerKey.toLowerCase();
        headerValue = httpHeader.get(headerKeyLowerCase);
        if (!Objects.isNull(headerValue)) {
            return headerValue;
        }
        for (Map.Entry<String, String> httpHeaderEntry : httpHeader.entrySet()) {
            if (headerKey.equalsIgnoreCase(httpHeaderEntry.getKey())) {
                return httpHeaderEntry.getValue();
            }
        }
        return null;
    }

    public static boolean equalsIgnoreCaseContentType(Map<String, String> httpHeader, String contentType) {
        String httpHeaderValue = getHttpHeaderValue(httpHeader, HttpHeaderKeyConstant.CONTENT_TYPE);
        if (null == httpHeaderValue) {
            return Boolean.FALSE;
        }
        String[] splitHeaderValue = httpHeaderValue.split(HttpHeaderFactoryConstant.SEMICOLON_STRING);
        String headerValueContentType = splitHeaderValue[0];
        if (headerValueContentType.equalsIgnoreCase(contentType)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static String getBoundary(Map<String, String> httpHeader) {
        String httpHeaderValue = getHttpHeaderValue(httpHeader, HttpHeaderKeyConstant.CONTENT_TYPE);
        if (null == httpHeaderValue) {
            return null;
        }
        String[] splitHeaderValue = httpHeaderValue.split(HttpHeaderFactoryConstant.SEMICOLON_STRING);
        for (String headerValue : splitHeaderValue) {
            if (headerValue.contains(HttpHeaderFactoryConstant.BOUNDARY)) {
                return headerValue.strip().split(HttpHeaderFactoryConstant.EQUALS_SIGN)[1];
            }
        }
        return null;
    }

    public static String createBoundary() {
        int boundaryInt = ThreadLocalRandom.current().nextInt();
        return new String(BoundaryConstant.BOUNDARY.getValue()) + boundaryInt;
    }

    public static String createFormDataContentTypeValue(String boundary) {
        StringBuilder contentTypeValue = new StringBuilder(HttpHeaderValueConstant.FORM_DATA);
        contentTypeValue.append(HttpHeaderFactoryConstant.SEMICOLON_STRING);
        contentTypeValue.append(HttpHeaderFactoryConstant.SPACE);
        contentTypeValue.append(HttpHeaderFactoryConstant.BOUNDARY);
        contentTypeValue.append(HttpHeaderFactoryConstant.EQUALS_SIGN);
        contentTypeValue.append(boundary);
        return contentTypeValue.toString();

    }


}
