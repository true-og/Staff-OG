package net.trueog.staffog.grpc.endpoint

import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.time.withTimeout
import proto.IpCheckReply
import proto.IpCheckRequest
import proto.IpCheckerGrpcKt
import proto.ipCheckReply
import java.time.Duration
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class IpCheckEndpoint : IpCheckerGrpcKt.IpCheckerCoroutineImplBase() {
    companion object {
        val checkIpMap = ConcurrentHashMap<UUID, CompletableDeferred<String>>()
    }

    override suspend fun checkIp(request: IpCheckRequest): IpCheckReply {
        val check = CompletableDeferred<String>()
        val uuid = UUID.fromString(request.uuid)
        // A newer check replaces an older one, which fails at once
        checkIpMap.put(uuid, check)?.cancel()
        val ip = try {
            withTimeout(Duration.ofMinutes(1)) { check.await() }
        } catch (_: CancellationException) {
            // Propagate if the caller itself went away
            currentCoroutineContext().ensureActive()
            // Timed out or replaced by a newer check
            throw StatusException(Status.DEADLINE_EXCEEDED)
        } finally {
            // Remove only this check's own entry
            checkIpMap.remove(uuid, check)
            // A late login must not answer a finished check
            check.cancel()
        }

        return ipCheckReply {
            this.ip = ip
        }
    }
}