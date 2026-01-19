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
import io.github.thierrysquirrel.jellyfish.thread.pool.ThreadPool;
import io.github.thierrysquirrel.jellyfish.thread.scheduled.one.ThreadScheduledOne;

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

    public static ThreadScheduledOne builderChannelHeartbeatThreadPoolExecutor() {
        return new ThreadScheduledOne();
    }

    public static ThreadPool builderHummingbirdServerThreadPoolExecutor() {
        return new ThreadPool(ThreadPoolExecutorBuilderConstant.HUMMINGBIRD_SERVER_CORE_POOL_SIZE);
    }

}
