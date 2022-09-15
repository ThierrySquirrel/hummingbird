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
package com.github.thierrysquirrel.hummingbird.core.builder.constant;

import com.github.thierrysquirrel.hummingbird.core.builder.ThreadPoolExecutorBuilder;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Classname: ThreadPoolExecutorConstant
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public final class ThreadPoolExecutorConstant {
    public static final ScheduledThreadPoolExecutor CHANNEL_HEARTBEAT = ThreadPoolExecutorBuilder.builderChannelHeartbeatThreadPoolExecutor();
    public static final ThreadPoolExecutor HUMMINGBIRD_SERVER = ThreadPoolExecutorBuilder.builderHummingbirdServerThreadPoolExecutor();

    private ThreadPoolExecutorConstant() {
    }
}
