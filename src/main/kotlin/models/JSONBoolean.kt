package models

import JSONValue
import visitors.Visitor

class JSONBoolean(val value: Boolean) : JSONValue() {


    override fun accept(v: Visitor) {
        v.visit(this)
    }

}