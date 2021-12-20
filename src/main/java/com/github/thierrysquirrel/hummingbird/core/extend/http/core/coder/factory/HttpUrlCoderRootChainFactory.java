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

import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.UrlCoderConstant;
import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory.chain.HttpUrlChain;

import java.util.Map;

/**
 * Classname: HttpUrlCoderRootChainFactory
 * Description:
 * Date: 2021/12/20 16:44
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public class HttpUrlCoderRootChainFactory {
    private HttpUrlCoderRootChainFactory() {
    }

    public static Map<String, String> createUrlMap(String url) {
        return UrlCoderFactory.builderUrlMap (url);
    }

    public static HttpUrlChain createUrl(String url, String key, String value) {
        StringBuilder urlBuilder = new StringBuilder (url);
        urlBuilder.append (UrlCoderConstant.QUESTION_MARK);
        UrlCoderFactory.builderUrl (urlBuilder, key, value);
        return new HttpUrlChain (urlBuilder);
    }


}
