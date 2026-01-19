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
package io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Classname: HttpRequestContext
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class HttpRequestContext {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private Map<String, String> httpHeader;
    private ByteBuffer httpBody;

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public Map<String, String> getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(Map<String, String> httpHeader) {
        this.httpHeader = httpHeader;
    }

    public ByteBuffer getHttpBody() {
        return httpBody;
    }

    public void setHttpBody(ByteBuffer httpBody) {
        this.httpBody = httpBody;
    }

    @Override
    public String toString() {
        return "HttpRequestContext{" +
                "httpRequest=" + httpRequest +
                ", httpResponse=" + httpResponse +
                ", httpHeader=" + httpHeader +
                ", httpBody=" + httpBody +
                '}';
    }
}
