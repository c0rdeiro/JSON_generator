package models

import JSONValue
import visitors.Visitor

class JSONArray(val elements: List<JSONValue>) : JSONValue() {


    val nElements: Int
        get() {
            var count = 0

            this.elements.forEach {
                if (it is JSONObject)
                    count += it.nElements
                else if (it is JSONArray)
                    count += it.nElements
                else
                    count++
            }
            return count
        }

    override fun accept(v: Visitor) {
        var counter = 0
        if (v.visit(this)) {

            elements.forEach {
                it.accept(v)
                if (counter < elements.size - 1)
                    v.visitSeparator()
                counter++
            }
            v.endVisit(this)
        }

    }

}