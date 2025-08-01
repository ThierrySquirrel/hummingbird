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
package io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory.chain;

import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.UrlCoderConstant;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory.UrlCoderFactory;
import lombok.Data;

/**
 * Classname: HttpUrlChain
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
@Data
public class HttpUrlChain {
    private StringBuilder urlBuilder;

    public HttpUrlChain(StringBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    public HttpUrlChain putUrl(String key, String value) {
        urlBuilder.append(UrlCoderConstant.AMPERSAND);
        UrlCoderFactory.builderUrl(urlBuilder, key, value);
        return this;
    }

    public String builder() {
        return urlBuilder.toString();
    }

}
