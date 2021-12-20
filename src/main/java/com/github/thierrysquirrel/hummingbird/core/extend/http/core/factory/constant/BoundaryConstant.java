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
package com.github.thierrysquirrel.hummingbird.core.extend.http.core.factory.constant;

/**
 * Classname: BoundaryConstant
 * Description:
 * Date: 2021/12/20 19:25
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public enum BoundaryConstant {
    /**
     * Boundary
     */
    BOUNDARY (new byte[]{-29, -125, -67, 40, -30, -100, -65, -17, -66, -97, -30, -106, -67, -17, -66, -97, 41, -29, -125, -114});
    private final byte[] value;

    BoundaryConstant(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }
}
