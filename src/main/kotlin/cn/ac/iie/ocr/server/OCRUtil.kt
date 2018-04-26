package cn.ac.iie.ocr.server

import cn.ac.iie.ocr.data.EngineCallback
import cn.ac.iie.ocr.data.GsonType
import cn.ac.iie.ocr.data.OpticalCharacterRecognition
import cn.ac.iie.ocr.data.TeaData
import cn.ac.iie.ocr.http.MyHttpClient
import cn.ac.iie.ocr.server.OCRServer.obs
import com.google.gson.GsonBuilder
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

class OCRUtil: Runnable {

    private val http = MyHttpClient()
    private val gson = GsonBuilder().disableHtmlEscaping().serializeNulls().create()

    private val t4j = TesseractUtil()

    companion object {
        val linke  = LinkedBlockingQueue<ArrayList<TeaData>>()
        private var start = AtomicBoolean(true)
        fun stop() {
            start.set(false)
        }
    }

    override fun run() {
        do {
            val list = linke.take()
            val (url, result) = ocr(list)
            http.postString(url!! ,result)
        } while (start.get())
    }

    fun ocr(list: ArrayList<TeaData>): OCR {
        var url: String? = null
        val result = arrayListOf<EngineCallback<OpticalCharacterRecognition.Content>>()
        list.forEach {
            url = it.callback
            val byte = obs.get(it.url!!.split("=")[1])
            val letter = t4j.imageToText(byte)
            result.add(EngineCallback(it.id, it.url, content = OpticalCharacterRecognition.Content(letter, "1")))
        }
        return OCR(url, gson.toJson(result))
    }

    data class OCR(val url: String?, val result: String)
}