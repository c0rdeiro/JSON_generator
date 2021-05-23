package UI

import JSONValue

interface IconSetup {

    fun getText(value : JSONValue): String
    fun getPath(value: JSONValue): String
}