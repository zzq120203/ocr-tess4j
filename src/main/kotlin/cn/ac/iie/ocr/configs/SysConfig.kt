package cn.ac.iie.ocr.configs

import com.google.gson.GsonBuilder
import java.io.File

object ConfLoading {
    internal var config: ConfigAll? = null

    fun init(path: String): ConfigAll {
        val gson = GsonBuilder().disableHtmlEscaping().create()
        val file = File(path)
        config = gson.fromJson(file.readText(Charsets.UTF_8), ConfigAll::class.java)
        return config ?: ConfigAll()
    }
}

data class ConfigAll(
        val linUrl: String = "http://localhost:8099",
        val contextPath: String = "tea",
        val port: Int = 8099,
        val urls: String = "STA://localhost:20099",
        val authToken: String? = null,
        val maxTotal: Int = 8,
        val szClientUrl: String = "STL://localhost:20099",
        val sbClientUrl: String = "STL://localhost:20099",
        val effectValue: Float = 0.0F,
        val tessData: String = "configs/tessdata"
)

fun config(init: Boolean = true) = if (init) ConfLoading.config ?: throw RuntimeException("configs no init") else ConfigAll()