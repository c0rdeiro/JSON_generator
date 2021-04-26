package visitors

import JSONNumber
import JSONString
import JSONValue
import models.*

class PrintJSONVisitor : Visitor {

    //TODO: refactor commas print
    var output: String = ""
    override fun visit(obj: JSONObject): Boolean {

        output +="{ "

        return true
    }

    override fun endVisit(obj: JSONObject) {

        output +=" }"


    }
    override fun visit(array: JSONArray): Boolean {
        output +="[ "

        return true
    }

    override fun endVisit(array: JSONArray) {
        output +=" ]"
    }
    override fun visit(str: JSONString) {
        output +="\"${str.value}\""
    }

    override fun visit(n: JSONNull) {
        output +="null"
    }

    override fun visit(bool: JSONBoolean) {
        output +="${bool.value}"

    }

    override fun visit(number: JSONNumber) {
        output +="${number.value}"
    }


    override fun visit(key: JSONKey) {
        output += "\"${key.value}\": "
    }

    override fun visitSeparator() {
        output += ", "
    }

}