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
package com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.builder;

import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpResponse;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpEditionConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpStatusCodeConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpStatusConstant;

/**
 * Classname: HttpResponseBuilder
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HttpResponseBuilder {
    private HttpResponseBuilder() {
    }

    public static HttpResponse builderHttpResponse(String httpEdition, String httpStatusCode, String httpStatus) {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHttpEdition(httpEdition);
        httpResponse.setHttpStatusCode(httpStatusCode);
        httpResponse.setHttpStatus(httpStatus);
        return httpResponse;
    }

    public static HttpResponse builderDefault() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHttpEdition(HttpEditionConstant.DEFAULT_EDITION);
        httpResponse.setHttpStatusCode(HttpStatusCodeConstant.SUCCESS);
        httpResponse.setHttpStatus(HttpStatusConstant.OK);
        return httpResponse;
    }
}
