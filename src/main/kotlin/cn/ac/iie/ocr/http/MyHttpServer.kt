package cn.ac.iie.ocr.http

import cn.ac.iie.ocr.configs.ConfLoading
import cn.ac.iie.ocr.configs.config
import cn.ac.iie.ocr.data.GsonType
import cn.ac.iie.ocr.data.Status
import cn.ac.iie.ocr.data.SysCallback
import cn.ac.iie.ocr.data.TeaData
import cn.ac.iie.ocr.server.OCRUtil
import com.google.gson.GsonBuilder
import io.javalin.Javalin
import org.slf4j.LoggerFactory

/**
 * HTTP接口服务
 */
class MyHttpServer {

    private val log = LoggerFactory.getLogger(MyHttpServer::class.java)

    private val gson = GsonBuilder().disableHtmlEscaping().serializeNulls().create()

    private val jLin = Javalin.create()

    fun start(port: Int = 8099) {
        jLin.port(port).contextPath("${config().contextPath}/").defaultCharacterEncoding("utf-8").start()

        jLin.get("config") { ctx ->
            val path = ctx.queryParam("path")
            path?.let {
                ConfLoading.init(it)
            }
            ctx.result(gson.toJson(config()))
        }.post("ocr") { ctx ->
            val body = ctx.body()
            val list: ArrayList<TeaData> = gson.fromJson(body, GsonType.teaType)
            OCRUtil.linke.add(list)
            ctx.result("")
        }.error(404) { ctx ->
            val callback = SysCallback(status = Status("404", "地址错误 -> ${config().linUrl}/${config().contextPath}/***"))
            ctx.result(gson.toJson(callback))
        }
    }

    fun stop() {
        jLin.stop()
    }

}