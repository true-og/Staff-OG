package net.trueog.staffog

import net.trueog.staffog.StaffOG.Companion.plugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class Config
private constructor(
    val port: Int,
    val sslEnabled: Boolean,
    val trustCa: String?,
    val cert: String?,
    val privateKey: String?,
) {
    companion object {
        fun create(): Config? {
            plugin.saveDefaultConfig()
            val file = File(plugin.dataFolder, "config.yml")
            val yamlConfig = YamlConfiguration.loadConfiguration(file)

            val port = yamlConfig.parseKeyAs<Int>("port")

            val sslEnabled = yamlConfig.parseKeyAs<Boolean>("sslEnabled")
            val trustCa = yamlConfig.parseKeyAs<String>("trustCa")
            val cert = yamlConfig.parseKeyAs<String>("cert")
            val privateKey = yamlConfig.parseKeyAs<String>("privateKey")

            if (port == null || sslEnabled == null) {
                return null
            }

            if (sslEnabled && (trustCa == null || cert == null || privateKey == null)) {
                plugin.logger.severe("SSL is enabled, but missing one or more options: trustCa, cert, or privateKey")
                return null
            }

            return Config(port, sslEnabled, trustCa, cert, privateKey)
        }

        private inline fun <reified T> YamlConfiguration.parseKeyAs(key: String): T? {
            if (!this.contains(key) && null is T) return null
            return this.get(key) as? T
                ?: run {
                    plugin.logger.severe("Failed to parse config option \"$key\" as ${T::class.simpleName}")
                    return null
                }
        }
    }

}