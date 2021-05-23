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

    fun addPair(key: JSONKey, value: JSONValue){
        elements[key] = value
    }

    fun addManyPairs(hashMap: HashMap<JSONKey, JSONValue>){
        elements.putAll(hashMap)
    }

    fun removeByKey(key: JSONKey){
        elements.remove(key)
    }
}