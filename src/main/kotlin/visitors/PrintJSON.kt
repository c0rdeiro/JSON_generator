package visitors

import JSONNumber
import JSONString
import models.*

class PrintJSON : Visitor {

    var output: String = ""
    override fun visit(obj: JSONObject): Boolean {

        output +="{"

        return true
    }

    override fun endVisit(obj: JSONObject) {

        output +="}"
        output.replace(" ,}", "}")


    }
    override fun visit(array: JSONArray): Boolean {
        output +="["

        return true
    }

    override fun endVisit(array: JSONArray) {
        output = output.substring(0, output.length-1)
        output +="]"
    }
    override fun visit(str: JSONString) {
        output +=" \"${str.value}\" ,"
    }

    override fun visit(value: JSONNull) {
        output +=" null ,"
    }

    override fun visit(bool: JSONBoolean) {
        output +=" ${bool.value} ,"

    }

    override fun visit(number: JSONNumber) {
        output +=" ${number.value} ,"
    }


    override fun visit(value: JSONKey) {
        output += "\"${value.value}\": "
    }

}