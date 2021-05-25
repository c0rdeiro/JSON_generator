package ui.IconSetups

import JSONValue

interface IconSetup {

    fun getText(value : JSONValue): String
    fun getPath(value: JSONValue): String
    fun getIconWidth(value: JSONValue): Int
    fun getIconHeight(value: JSONValue): Int
    fun toExclude(value: JSONValue): Boolean = false
}