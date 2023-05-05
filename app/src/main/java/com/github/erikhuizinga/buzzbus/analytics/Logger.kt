package com.github.erikhuizinga.buzzbus.analytics

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.io.Closeable
import kotlinx.coroutines.launch
import org.json.JSONObject

interface Logger {
    fun logEvent(vararg params: Pair<String, Any?>)
    fun logEvent(params: Map<String, Any?>)
}

class JitsuLogger private constructor(
    endpoint: String,
    private val apiKey: String,
) : Logger, Closeable {
    private val client = HttpClient {
        defaultRequest {
            contentType(ContentType.Application.Json)
            header("X-Auth-Token", apiKey)
        }
    }

    private val eventsEndpoint = "${endpoint.dropLastWhile { it == '/' }}/api/v1/s2s/event"

    override fun logEvent(params: Map<String, Any?>) {
        client.launch {
            val response = client.post(eventsEndpoint) {
                setBody((JSONObject.wrap(params) ?: null).toString())
            }
            Log.d("JitsuLogger", response.toString())
        }
    }

    override fun logEvent(vararg params: Pair<String, Any?>) = logEvent(params.toMap())
    override fun close() = client.close()

    companion object {
        private var apiKey: String? = null
        private var endpoint: String? = null
        private var instance: JitsuLogger? = null

        fun getInstance(endpoint: String, apiKey: String): Logger {
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
