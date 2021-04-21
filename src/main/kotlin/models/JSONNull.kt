package models

import JSONValue
import visitors.Visitor

class JSONNull() : JSONValue() {

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}