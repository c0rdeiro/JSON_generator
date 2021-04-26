package models

import JSONValue
import visitors.Visitor

class JSONArray(val elems: Collection<JSONValue>) : JSONValue() {


    override fun accept(v: Visitor) {

        if (v.visit(this)) {

            elems.forEach { it.accept(v) }
            v.endVisit(this)
        }

    }
}