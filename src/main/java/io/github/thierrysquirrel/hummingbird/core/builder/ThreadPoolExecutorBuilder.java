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
package io.github.thierrysquirrel.hummingbird.core.builder;

import io.github.thierrysquirrel.hummingbird.core.builder.constant.ThreadPoolExecutorBuilderConstant;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * Classname: ThreadPoolExecutorBuilder
 * Description:
 * Date:2024/8/8
 *
 * @author ThierrySquirrel
 * @since JDK21
 **/
public class ThreadPoolExecutorBuilder {
    private ThreadPoolExecutorBuilder() {
    }

    public static ScheduledThreadPoolExecutor builderChannelHeartbeatThreadPoolExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(ThreadPoolExecutorBuilderConstant.CHANNEL_HEARTBEAT).build();
        return new ScheduledThreadPoolExecutor(ThreadPoolExecutorBuilderConstant.CHANNEL_HEARTBEAT_CORE_POOL_SIZE, threadFactory);
    }

    public static ThreadPoolExecutor builderHummingbirdServerThreadPoolExecutor() {
        var threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(ThreadPoolExecutorBuilderConstant.HUMMINGBIRD_SERVER).build();
        return new ThreadPoolExecutor(ThreadPoolExecutorBuilderConstant.HUMMINGBIRD_SERVER_CORE_POOL_SIZE,
                ThreadPoolExecutorBuilderConstant.HUMMINGBIRD_SERVER_MAXIMUM_POOL_SIZE,
                ThreadPoolExecutorBuilderConstant.KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory
        );
    }

}
