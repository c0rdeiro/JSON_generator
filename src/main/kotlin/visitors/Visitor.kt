package visitors

import JSONNumber
import JSONString
import JSONValue
import models.*

interface Visitor {

    fun visit(obj: JSONObject): Boolean = true
    fun endVisit(obj: JSONObject){}
    fun visit(array: JSONArray): Boolean = true
    fun endVisit(array: JSONArray){}
    fun visit(number: JSONNumber){}
    fun visit(str: JSONString){}
    fun visit(bool: JSONBoolean){}
    fun visit(n: JSONNull){}
    fun visit(key: JSONKey){}
    fun visitSeparator(){}
}