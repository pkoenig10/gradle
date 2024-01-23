/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.internal.restricteddsl.plugins

/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.h0tk3y.kotlin.staticObjectNotation.AccessFromCurrentReceiverOnly
import com.h0tk3y.kotlin.staticObjectNotation.Adding
import com.h0tk3y.kotlin.staticObjectNotation.Builder
import com.h0tk3y.kotlin.staticObjectNotation.Configuring
import com.h0tk3y.kotlin.staticObjectNotation.Restricted
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec


class PluginsTopLevelReceiver {
    val plugins = PluginsCollectingPluginsBlock()

    @Configuring
    @AccessFromCurrentReceiverOnly
    fun plugins(configure: PluginsCollectingPluginsBlock.() -> Unit) {
        configure(plugins)
    }
}


class PluginsCollectingPluginsBlock() : PluginDependenciesSpec {
    val specs: List<MutablePluginDependencySpec>
        get() = _specs

    private
    val _specs = mutableListOf<MutablePluginDependencySpec>()

    @Adding
    override fun id(id: String): MutablePluginDependencySpec = MutablePluginDependencySpec(id).also(_specs::add)

    @Adding
    fun kotlin(id: String) = id("org.jetbrains.kotlin.$id")
}


class MutablePluginDependencySpec(
    val id: String
) : PluginDependencySpec {
    @Restricted
    var version: String? = null
        private
        set

    @Restricted
    var apply: Boolean = true
        private
        set

    @Builder
    override fun version(version: String?): MutablePluginDependencySpec {
        this.version = version
        return this
    }

    @Builder
    override fun apply(apply: Boolean): MutablePluginDependencySpec {
        this.apply = apply
        return this
    }
}
