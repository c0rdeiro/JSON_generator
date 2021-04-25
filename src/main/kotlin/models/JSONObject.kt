package models

import JSONValue
import visitors.Visitor

class JSONObject(val elems: MutableMap<JSONKey, JSONValue>) : JSONValue() {


    val nProperties: Int
        get() = elems.keys.size


    val nElements: Int
        get() {
            var count = 0

            this.elems.forEach {
                if (it.value is JSONObject)
                    count += (it.value as JSONObject).nElements
                else
                    count++
            }

            return count
        }

    override fun accept(v: Visitor) {


        if (v.visit(this)) {
            elems.forEach {

                v.visit(it.key)
                it.value.accept(v)
            }

            v.endVisit(this)
        }

    }
}