package cn.ac.iie.ocr.data

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.ArrayList

object GsonType {

    val teaType: Type = object : TypeToken<ArrayList<TeaData>>() {}.type
}
