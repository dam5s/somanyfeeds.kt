package com.somanyfeeds.httpgateway

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.slf4j.LoggerFactory

public trait HttpGateway {
    fun get(url: String): String
}

public class OkHttpGateway(val client: OkHttpClient): HttpGateway {
    private val logger = LoggerFactory.getLogger(javaClass<OkHttpGateway>())

    override fun get(url: String): String {
        logger.debug("GET {}", url)

        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val body = response.body().string()

        logger.debug("Response: {}", response)
        logger.debug("Body:\n\n{}\n\n", body)

        return body
    }
}
