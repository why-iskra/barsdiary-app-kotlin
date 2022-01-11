import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec

object Sugar {
    fun include(scope: PluginDependenciesSpec, plugin: String) {
        scope.id(plugin)
    }

    fun include(scope: PluginDependenciesSpec, plugin: PluginWithVersion) {
        scope.id(plugin.id) version plugin.version
    }
}
