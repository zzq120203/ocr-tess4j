package cn.ac.iie.ocr.server

import cn.ac.iie.ocr.configs.config
import com.carrotsearch.labs.langid.LangIdV3
import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import org.slf4j.LoggerFactory

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.regex.Pattern

/**
 * Hello world!
 *
 */
class TesseractUtil {
    private val log = LoggerFactory.getLogger(TesseractUtil::class.java)
    private var tesseract = Tesseract()
    private var lang = LangIdV3()
    private val pattern: Pattern = Pattern.compile("[\u4e00-\u9fa5]")

    init {
        tesseract.setDatapath(config().tessData)
    }

    private fun formatting(str: String): String? {
        var result: String? = str.replace("\\s".toRegex(), "")
        val langcode = lang.classify(result!!, true).langCode
        when (langcode) {
            "zh" -> {
                val m = pattern.matcher(str)
                var effective = ""
                while (m.find()) {
                    effective += m.group()
                }
                val effectValue = effective.length.toFloat() / result.length.toFloat()
                result = if (effectValue > config().effectValue) result.replace("['′`\"”″“]".toRegex(), "") else null
            }
            "en" -> {

            }
            else -> result = null
        }
        return result
    }

    fun imageToText(bs: ByteArray, type: String = "uig+bod+eng+chi_sim"): String? {
        val bi = byteToBI(bs)
        var result: String? = null

        try {
            if (bi != null) {
                tesseract.setLanguage(type)
                result = tesseract.doOCR(bi)
            }
        } catch (e: TesseractException) {
            log.error(e.message, e)
        }

        return if (result != null) {
            formatting(result)
        } else result

    }

    private fun byteToBI(bs: ByteArray): BufferedImage? {
        val input = ByteArrayInputStream(bs)
        var image: BufferedImage? = null
        try {
            image = ImageIO.read(input)
        } catch (e: IOException) {
            log.error(e.message, e)
        }
        return image
    }

}
