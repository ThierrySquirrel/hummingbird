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

import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.client.factory.chain.constant.FormDataChainConstant;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.HttpCoderConstant;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.constant.HttpFormDataCoderConstant;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.coder.factory.HttpFormDataBodyFactory;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.HttpRequestContext;
import io.github.thierrysquirrel.hummingbird.core.extend.http.core.domain.constant.HttpHeaderKeyConstant;
import io.github.thierrysquirrel.hummingbird.core.facade.ByteBufferFacade;
import io.github.thierrysquirrel.hummingbird.core.facade.builder.ByteBufferFacadeBuilder;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Classname: FormDataChain
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
@Data
public class FormDataChain {
    private HttpRequestContext httpRequestContext;
    private String beginBoundary;
    private String endBoundary;
    private ByteBufferFacade httpBody;

    public FormDataChain(HttpRequestContext httpRequestContext, String beginBoundary, String endBoundary) {
        this.httpRequestContext = httpRequestContext;
        this.beginBoundary = beginBoundary;
        this.endBoundary = endBoundary;
        this.httpBody = ByteBufferFacadeBuilder.builderDirectByteBufferFacade();
    }

    public FormDataChain putText(String key, String value) {
        putBeginBoundary();
        putContentDispositionText(key);
        putTag();
        putTextBody(value);
        return this;
    }

    public FormDataChain putFile(String key, String filePath, String fileContentType) throws IOException {
        putBeginBoundary();
        File file = new File(filePath);
        putContentDispositionFile(key, file.getName());
        putTag();
        putContentType(fileContentType);
        putFileBody(filePath);
        return this;
    }

    public void builder() {
        putEndBoundary();
        httpBody.flip();
        httpRequestContext.getHttpHeader().put(HttpHeaderKeyConstant.CONTENT_LENGTH, httpBody.length() + "");
        httpRequestContext.setHttpBody(httpBody.getByteBuffer());
    }

    private void putContentDispositionFile(String key, String fileName) {
        putContentDispositionText(key);
        httpBody.putString(HttpFormDataCoderConstant.SEMICOLON_STRING);
        httpBody.putString(FormDataChainConstant.SPACE);

        httpBody.putString(FormDataChainConstant.FILE_NAME);
        httpBody.putString(FormDataChainConstant.EQUALS_SIGN);
        String fileNameQuotationMark = textPutQuotationMark(fileName);
        httpBody.putString(fileNameQuotationMark);
    }

    private void putContentType(String fileContentType) {
        httpBody.putString(FormDataChainConstant.CONTENT_TYPE);
        httpBody.putString(HttpFormDataCoderConstant.COLON);
        httpBody.putString(FormDataChainConstant.SPACE);
        httpBody.putString(fileContentType);
        putTag();
    }

    private void putFileBody(String filePath) throws IOException {
        putTag();
        ByteBuffer fileBody = HttpFormDataBodyFactory.readFile(filePath);
        httpBody.put(fileBody);
        putTag();
    }

    private void putContentDispositionText(String name) {
        httpBody.putString(FormDataChainConstant.CONTENT_DISPOSITION);
        httpBody.putString(HttpFormDataCoderConstant.COLON);
        httpBody.putString(FormDataChainConstant.SPACE);

        httpBody.putString(FormDataChainConstant.FORM_DATA);
        httpBody.putString(HttpFormDataCoderConstant.SEMICOLON_STRING);
        httpBody.putString(FormDataChainConstant.SPACE);

        httpBody.putString(FormDataChainConstant.NAME);
        httpBody.putString(FormDataChainConstant.EQUALS_SIGN);
        String nameQuotationMark = textPutQuotationMark(name);
        httpBody.putString(nameQuotationMark);
    }

    private void putTextBody(String text) {
        putTag();
        httpBody.putString(text);
        putTag();
    }

    private void putBeginBoundary() {
        httpBody.putString(beginBoundary);
        putTag();
    }

    private void putEndBoundary() {
        httpBody.putString(endBoundary);
        putTag();
    }

    private void putTag() {
        httpBody.putByte(HttpCoderConstant.CARRIAGE_RETURN);
        httpBody.putByte(HttpCoderConstant.LINE_FEED);
    }

    private String textPutQuotationMark(String text) {
        StringBuilder stringBuilder = new StringBuilder(FormDataChainConstant.QUOTATION_MARK);
        stringBuilder.append(text);
        stringBuilder.append(FormDataChainConstant.QUOTATION_MARK);
        return stringBuilder.toString();
    }
}
