package models

import JSONValue
import visitors.Visitor

class JSONArray(val elems: MutableCollection<JSONValue>) : JSONValue() {


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

    fun addValue(value: JSONValue){
        elems.add(value)
    }

    fun addMany(arr: Collection<JSONValue>){
        elems.addAll(arr)
    }

    fun removeValue(value: JSONValue){
        elems.remove(value)
    }
}