package net.trueog.staffog

import io.grpc.Server
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import net.trueog.staffog.grpc.endpoint.IpCheckEndpoint
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.LoggerConfig
import org.apache.logging.log4j.Level
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class StaffOG : JavaPlugin() {
    companion object {
        lateinit var plugin: StaffOG
        lateinit var config: Config
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    private lateinit var grpcServer: Server
    fun isGrpcServerInitialized() = ::grpcServer.isInitialized

    override fun onEnable() {
        suppressNettyError()

        plugin = this

        Companion.config = Config.create() ?: run {
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        this.server.pluginManager.registerEvents(Events(), this)

        val builder = NettyServerBuilder.forPort(Companion.config.port)
            .addService(IpCheckEndpoint())

        if (Companion.config.sslEnabled) {
            builder.sslContext(
                GrpcSslContexts
                    .forServer(File(Companion.config.cert!!), File(Companion.config.privateKey!!))
                    .trustManager(File(Companion.config.trustCa!!))
                    .clientAuth(ClientAuth.REQUIRE)
                    .build()
            )
        }

        grpcServer = builder.build().start()
    }

    override fun onDisable() {
        if (isGrpcServerInitialized()) {
            grpcServer.shutdownNow()
            grpcServer.awaitTermination()
        }

        scope.cancel()
        runBlocking { scope.coroutineContext[Job]?.join() }
    }

    // Hack: suppress the "Failed to submit a listener notification task. Event loop shut down?" error on shutdown
    private fun suppressNettyError() {
        val ctx = LogManager.getContext(false) as LoggerContext
        val config = ctx.configuration

        val loggerName = "net.trueog.staffog.shadow.io.grpc.netty.shaded.io.netty.util.concurrent.DefaultPromise"
        val loggerConfig = LoggerConfig(loggerName, Level.OFF, false)
        config.addLogger(loggerName, loggerConfig)
        ctx.updateLoggers(config)
    }
}
