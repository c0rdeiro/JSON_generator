package ui.IconSetups

import JSONNumber
import JSONString
import JSONValue
import models.JSONArray
import models.JSONBoolean
import models.JSONNull
import models.JSONObject
import java.lang.IllegalArgumentException

class DefaultIconSetup: IconSetup {

    override fun getText(value: JSONValue): String {
        return when(value){

            is JSONString -> "\"${value.value}\""
            is JSONNumber -> value.value.toString()
            is JSONBoolean -> value.value.toString()
            is JSONNull -> "null"
            is JSONObject -> "(object)"
            is JSONArray -> "(array)"
            else -> throw IllegalArgumentException("Unsupported data type.")

        }
    }

    override fun getPath(value: JSONValue): String {
        return if(isLeaf(value))
            "src/main/resources/file.png"
        else "src/main/resources/folder.png"
    }

    override fun getIconWidth(value: JSONValue): Int {
        return if(isLeaf(value))
            25
        else 20
    }
    override fun getIconHeight(value: JSONValue): Int {
        return if(isLeaf(value))
            25
        else 20
    }

    override fun toExclude(value: JSONValue): Boolean {
        return when (value) {
            is JSONObject -> value.nElements < 2
            is JSONString -> true
            else -> false
        }
    }


    private fun isLeaf(value: JSONValue) =
        !(value is JSONObject || value is JSONArray)




}