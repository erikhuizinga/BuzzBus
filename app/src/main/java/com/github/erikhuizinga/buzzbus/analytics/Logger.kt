package com.github.erikhuizinga.buzzbus.analytics

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import java.io.Closeable
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

interface Logger {
    fun logEvent(name: String, vararg params: Pair<String, Any?>)
    fun logEvent(name: String, params: Map<String, Any?>)
}

class JitsuLogger private constructor(endpoint: URL, apiKey: String) : Logger, Closeable {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val client = HttpClient(CIO)

    override fun logEvent(name: String, vararg params: Pair<String, Any?>) =
        logEvent(name, params.toMap())

    override fun logEvent(name: String, params: Map<String, Any?>) {
        coroutineScope.launch {
            val response = client.get("https://ktor.io/")
            Log.d("JitsuLogger", response.status.toString())
            Log.d("JitsuLogger", response.toString())
        }
    }

    override fun close() {
        coroutineScope.cancel()
        client.close()
    }

    companion object {
        private var apiKey: String? = null
        private var endpoint: URL? = null
        private var instance: JitsuLogger? = null

        fun getInstance(endpoint: URL, apiKey: String): Logger {
            val currentInstance = instance
            return if (currentInstance == null || apiKey != this.apiKey || endpoint != this.endpoint) {
                currentInstance?.close()
                val instance = JitsuLogger(endpoint, apiKey)
                this.apiKey = apiKey
                this.endpoint = endpoint
                this.instance = instance
                instance
            } else {
                currentInstance
            }
        }
    }
}
