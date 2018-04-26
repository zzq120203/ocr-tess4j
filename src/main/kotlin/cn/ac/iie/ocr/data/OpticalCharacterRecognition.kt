package cn.ac.iie.ocr.data

/**
 * 图像识别数据类(Optical Character Recognition)
 */
class OpticalCharacterRecognition {
    data class Content(var letter: String? = null, var score: String? = null)
}