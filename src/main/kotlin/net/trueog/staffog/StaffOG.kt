package net.trueog.staffog

import io.grpc.Server
import io.grpc.ServerBuilder
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
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.TimeUnit

class StaffOG : JavaPlugin() {
    companion object {
        lateinit var plugin: StaffOG
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    private lateinit var grpcServer: Server

    override fun onEnable() {
        suppressNettyError()

        plugin = this

        this.server.pluginManager.registerEvents(Events(), this)

        grpcServer = ServerBuilder.forPort(65226)
            .addService(IpCheckEndpoint())
            .build().start()
    }

    override fun onDisable() {
        grpcServer.shutdownNow()
        grpcServer.awaitTermination()

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
