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
package com.github.thierrysquirrel.hummingbird.core.builder.constant;

/**
 * Classname: ThreadPoolExecutorBuilderConstant
 * Description:
 * Date: 2021/7/29 21:27
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public final class ThreadPoolExecutorBuilderConstant {
    public static final int KEEP_ALIVE_TIME = 0;

    public static final String CHANNEL_HEARTBEAT = "channel_heartbeat-thread-%d";
    public static final int CHANNEL_HEARTBEAT_CORE_POOL_SIZE = 1;
    public static final long CHANNEL_HEARTBEAT_DELAY = 256;

    public static final String HUMMINGBIRD_SERVER = "hummingbird-server-thread-%d";
    public static final int HUMMINGBIRD_SERVER_CORE_POOL_SIZE = Runtime.getRuntime ().availableProcessors () * 2;
    public static final int HUMMINGBIRD_SERVER_MAXIMUM_POOL_SIZE = Runtime.getRuntime ().availableProcessors () * 2;

    private ThreadPoolExecutorBuilderConstant() {
    }
}
