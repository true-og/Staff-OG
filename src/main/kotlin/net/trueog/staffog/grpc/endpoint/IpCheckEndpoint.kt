package net.trueog.staffog.grpc.endpoint

import io.grpc.Status
import io.grpc.StatusException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import net.trueog.staffog.StaffOG
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
        checkIpMap[uuid] = check
        StaffOG.scope.launch {
            delay(Duration.ofMinutes(1))
            check.cancel()
            checkIpMap.remove(uuid)
        }
        val ip = try {
             check.await()
        } catch (_: CancellationException) {
            throw StatusException(Status.DEADLINE_EXCEEDED)
        }

        checkIpMap.remove(uuid)

        return ipCheckReply {
            this.ip = ip
        }
    }
}