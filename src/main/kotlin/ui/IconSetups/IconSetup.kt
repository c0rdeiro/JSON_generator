package ui.IconSetups

import JSONValue

interface IconSetup {

    fun getText(value : JSONValue): String
    fun getPath(value: JSONValue): String
    fun toExclude(value: JSONValue): Boolean = false
}