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
package io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant;

/**
 * Classname: HttpHeaderValueConstant
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public final class HttpHeaderValueConstant {
    private HttpHeaderValueConstant() {
    }

    public static final String JSON = "application/json";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String FORM_DATA = "multipart/form-data";
    public static final String FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String OCTET_STREAM = "application/octet-stream";

    public static final String KEEP_ALIVE = "keep-alive";

    public static final String JPG = "image/jpeg";
    public static final String PNG = "image/png";

}
