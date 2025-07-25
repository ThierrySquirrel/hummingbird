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
package io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.client.factory.chain;

import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.client.factory.FormUrlencodedFactory;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.UrlCoderConstant;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderKeyConstant;
import io.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import lombok.Data;

/**
 * Classname: FormUrlencodedChain
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
@Data
public class FormUrlencodedChain {
    private ByteBufferFacade httpBody;
    private HttpRequestContext httpRequestContext;

    public FormUrlencodedChain(ByteBufferFacade httpBody, HttpRequestContext httpRequestContext) {
        this.httpBody = httpBody;
        this.httpRequestContext = httpRequestContext;
    }

    public FormUrlencodedChain putText(String key, String value) {
        httpBody.putString(UrlCoderConstant.AMPERSAND);
        FormUrlencodedFactory.putText(httpBody, key, value);
        return this;
    }

    public void builder() {
        httpBody.flip();
        int length = httpBody.length();
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_LENGTH, length + "");
        httpRequestContext.setHttpBody(httpBody.getByteBuffer());
    }
}
