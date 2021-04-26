package models

import JSONValue
import visitors.Visitor

class JSONArray(val elems: Collection<JSONValue>) : JSONValue() {


    override fun accept(v: Visitor) {
        var counter = 0
        if (v.visit(this)) {

            elems.forEach {
                it.accept(v)
                if(counter < elems.size-1)
                    v.visitSeparator()
                counter ++
            }
            v.endVisit(this)
        }

    }
}