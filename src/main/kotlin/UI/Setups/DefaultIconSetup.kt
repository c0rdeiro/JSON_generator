package UI.Setups

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
        if(isLeaf(value))
            return "src/main/resources/file.png"
        else return "src/main/resources/folder.png"
    }

    override fun toExclude(value: JSONValue): Boolean {
        return when (value) {
            is JSONObject -> value.nElements < 2
            is JSONArray -> value.nElements > 2
            else -> false
        }
    }


    fun isLeaf(value: JSONValue) =
        !(value is JSONObject || value is JSONArray)




}