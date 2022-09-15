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
package com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory;

import com.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.UrlCoderConstant;
import com.google.common.collect.Maps;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Classname: UrlCoderFactory
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class UrlCoderFactory {

    private UrlCoderFactory() {
    }

    public static String decode(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }

    public static String encoder(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    public static void builderUrl(StringBuilder urlBuilder, String key, String value) {
        urlBuilder.append(UrlCoderFactory.encoder(key));
        urlBuilder.append(UrlCoderConstant.EQUALS_SIGN);
        urlBuilder.append(UrlCoderFactory.encoder(value));
    }

    public static Map<String, String> builderUrlMap(String url) {
        int index = url.indexOf(UrlCoderConstant.QUESTION_MARK);
        Map<String, String> urlMap = Maps.newConcurrentMap();
        if (index == -1) {
            return urlMap;
        }
        url = url.substring(index + 1);
        String[] urlSplit = url.split(UrlCoderConstant.AMPERSAND);
        for (String uriParam : urlSplit) {
            int substringIndex = uriParam.indexOf(UrlCoderConstant.EQUALS_SIGN);
            String key = uriParam.substring(0, substringIndex);
            String value = uriParam.substring(substringIndex + 1);
            urlMap.put(UrlCoderFactory.decode(key), UrlCoderFactory.decode(value));
        }
        return urlMap;
    }

}
