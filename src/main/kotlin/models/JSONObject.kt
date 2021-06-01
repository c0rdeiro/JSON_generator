package models

import JSONValue
import visitors.Visitor

class JSONObject(val elements: MutableMap<JSONKey, JSONValue>) : JSONValue() {


    val nProperties: Int
        get() = elements.keys.size


    val nElements: Int
        get() {
            var count = 0

            this.elements.forEach {
                if (it.value is JSONObject)
                    count += (it.value as JSONObject).nElements
                else if (it.value is JSONArray)
                    count += (it.value as JSONArray).nElements
                else
                    count++
            }

            return count
        }

    override fun accept(v: Visitor) {
        var counter = 0
        if (v.visit(this)) {
            elements.forEach {

                v.visit(it.key)
                it.value.accept(v)
                if(counter < elements.size-1)
                    v.visitSeparator()
                counter ++
            }

            v.endVisit(this)
        }
    }
}