package models

import JSONString
import visitors.Visitor

class JSONKey (key: String): JSONString(key){

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}