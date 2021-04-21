package models

import JSONValue
import visitors.Visitor

class JSONArray(val elems : Collection<JSONValue>? = null) : JSONValue() {


    override fun accept(v: Visitor) {


        if(v.visit(this)){
            if(!elems.isNullOrEmpty())
                elems.map{ it.accept(v) }

            v.endVisit(this)
        }

    }
}