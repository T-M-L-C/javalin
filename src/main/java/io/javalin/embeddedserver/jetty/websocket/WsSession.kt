/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.embeddedserver.jetty.websocket

import org.eclipse.jetty.websocket.api.*
import org.eclipse.jetty.websocket.common.WebSocketSession
import java.net.InetSocketAddress
import java.net.URLDecoder

class WsSession(session: Session) : Session {

    private val wss = session as WebSocketSession

    fun send(message: String) = wss.remote.sendString(message)

    fun queryParam(queryParam: String): String? = queryParams(queryParam)?.get(0)
    fun queryParams(queryParam: String): Array<String>? = queryParamMap()[queryParam]
    fun queryParamMap(): Map<String, Array<String>> {
        return wss.upgradeRequest!!.queryString.split("&").map { it.split("=") }.groupBy(
                { it[0] },
                { if (it.size > 1) URLDecoder.decode(it[1], "UTF-8") else "" }
        ).mapValues { it.value.toTypedArray() }
    }

    // interface overrides + equals/hash
    override fun close() = wss.close()
    override fun close(closeStatus: CloseStatus?) = wss.close(closeStatus)
    override fun close(statusCode: Int, reason: String?) = wss.close(statusCode, reason)
    override fun disconnect() = wss.disconnect()
    override fun getIdleTimeout() = wss.idleTimeout;
    override fun getLocalAddress(): InetSocketAddress? = wss.localAddress
    override fun getPolicy(): WebSocketPolicy? = wss.policy
    override fun getProtocolVersion(): String? = wss.protocolVersion
    override fun getRemote(): RemoteEndpoint? = wss.remote
    override fun getRemoteAddress(): InetSocketAddress? = wss.remoteAddress
    override fun getUpgradeRequest(): UpgradeRequest? = wss.upgradeRequest
    override fun getUpgradeResponse(): UpgradeResponse? = wss.upgradeResponse
    override fun isOpen() = wss.isOpen
    override fun isSecure() = wss.isSecure
    override fun setIdleTimeout(ms: Long) = wss.setIdleTimeout(ms)
    override fun suspend(): SuspendToken? = wss.suspend()
    override fun equals(other: Any?) = wss == (other as WsSession).wss
    override fun hashCode() = wss.hashCode()
}
