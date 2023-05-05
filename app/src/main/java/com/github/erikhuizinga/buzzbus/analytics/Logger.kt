package com.github.erikhuizinga.buzzbus.analytics

import java.io.Closeable
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

interface Logger {
    fun logEvent(name: String, vararg params: Pair<String, Any?>)
    fun logEvent(name: String, params: Map<String, Any?>)
}

class JitsuLogger private constructor(endpoint: URL, apiKey: String) : Logger, Closeable {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun logEvent(name: String, vararg params: Pair<String, Any?>) {
        // TODO
    }

    override fun logEvent(name: String, params: Map<String, Any?>) {
        // TODO
    }

    override fun close() {
        coroutineScope.cancel()
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
