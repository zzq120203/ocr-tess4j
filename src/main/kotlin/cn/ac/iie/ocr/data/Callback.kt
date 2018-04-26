package cn.ac.iie.ocr.data

data class SysCallback(var id: String? = null, var status: Status? = null)

data class EngineCallback<T>(var id: String? = null, var url: String? = null, var status: Status? = null, var content: T? = null)

data class Status(var code: String? = null, var failed: String? = null)