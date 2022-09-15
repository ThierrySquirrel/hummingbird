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
package com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.server.factory.domain.builder;

import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.server.factory.domain.HttpFormData;

import java.nio.ByteBuffer;

/**
 * Classname: HttpFormDataBuilder
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HttpFormDataBuilder {
    private HttpFormDataBuilder() {
    }

    public static HttpFormData builderHttpFormData(String value) {
        HttpFormData httpFormData = new HttpFormData();
        httpFormData.setValue(value);
        return httpFormData;
    }

    public static HttpFormData builderFileHttpFormData(String fileName, String fileContentType, ByteBuffer fileValue) {
        HttpFormData httpFormData = new HttpFormData();
        httpFormData.setFile(Boolean.TRUE);
        httpFormData.setFileName(fileName);
        httpFormData.setFileContentType(fileContentType);
        httpFormData.setFileValue(fileValue);
        return httpFormData;
    }
}
