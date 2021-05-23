package models

import JSONValue
import visitors.Visitor

class JSONArray(val elements: MutableCollection<JSONValue>) : JSONValue() {


    override fun accept(v: Visitor) {
        var counter = 0
        if (v.visit(this)) {

            elements.forEach {
                it.accept(v)
                if(counter < elements.size-1)
                    v.visitSeparator()
                counter ++
            }
            v.endVisit(this)
        }

    }

    fun addValue(value: JSONValue){
        elements.add(value)
    }

    fun addMany(arr: Collection<JSONValue>){
        elements.addAll(arr)
    }

    fun removeValue(value: JSONValue){
        elements.remove(value)
    }
}