package cn.ac.iie.ocr.server

import cn.ac.iie.client.OBSSClient
import cn.ac.iie.ocr.configs.ConfLoading
import cn.ac.iie.ocr.configs.config
import cn.ac.iie.ocr.http.MyHttpServer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import java.io.File

object OCRServer {

    val obs = OBSSClient()

    init {
        val logContext = LogManager.getContext(false) as LoggerContext
        val log4jFile = File("configs/log4j2.xml")
        logContext.configLocation = log4jFile.toURI()
        logContext.reconfigure()

        ConfLoading.init("configs/config.json")
        obs.init(config().szClientUrl, "TEST")
    }

    @JvmStatic
    fun main(args: Array<String>) {

        MyHttpServer().start(8098)

        Thread(OCRUtil()).start()
    }
}