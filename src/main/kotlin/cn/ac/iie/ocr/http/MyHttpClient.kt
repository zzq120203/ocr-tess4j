package cn.ac.iie.ocr.http

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * HTTP客户端，连接超时时间5s，响应时间20s
 */
class MyHttpClient(ct: Long = 5, rt: Long = 20) {
    private val client = OkHttpClient.Builder()
            .connectTimeout(ct, TimeUnit.SECONDS)
            .readTimeout(rt, TimeUnit.SECONDS)
            .build()

    private val MEDIA_TYPE_TEXT = MediaType.parse("text/plain")

    @Throws(IOException::class)
    fun syncGet(url: String): String? {
        val request = Request.Builder().url(url).build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("服务器端错误: " + response)
        }
        /*
        val responseHeaders = response.headers()
        for (i in 0 until responseHeaders.size()) {
            println(responseHeaders.name(i) + ": " + responseHeaders.value(i))
        }
        */

        return response.body()?.string()
    }


    @Throws(IOException::class)
    fun headers(url: String) {
        val request = Request.Builder().url(url).header("User-Agent", "My super agent").addHeader("Accept", "text/html").build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("服务器端错误: " + response)
        }

        System.out.println(response.header("Server"))
        System.out.println(response.headers("Set-Cookie"))
    }


    @Throws(IOException::class)
    fun postString(url: String, postBody: String): String? {
        val request = Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_TEXT, postBody)).build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("服务器端错误: " + response)
        }
        return response.body()?.string()
    }

    @Throws(IOException::class)
    fun postStream(url: String, postBody: String): String? {
        val requestBody = object : RequestBody() {
            override fun contentType(): MediaType? {
                return MEDIA_TYPE_TEXT
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                sink.writeUtf8(postBody)
            }

            @Throws(IOException::class)
            override fun contentLength(): Long {
                return postBody.length.toLong()
            }
        }

        val request = Request.Builder().url(url).post(requestBody).build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("服务器端错误: " + response)
        }

        return response.body()?.string()
    }
}