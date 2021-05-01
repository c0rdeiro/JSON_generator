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
        var counter = 0
        if (v.visit(this)) {
            elems.forEach {

                v.visit(it.key)
                it.value.accept(v)
                if(counter < elems.size-1)
                    v.visitSeparator()
                counter ++
            }

            v.endVisit(this)
        }

    }

    fun addPair(key: JSONKey, value: JSONValue){
        elems[key] = value
    }

    fun addManyPairs(hashMap: HashMap<JSONKey, JSONValue>){
        elems.putAll(hashMap)
    }

    fun removeByKey(key: JSONKey){
        elems.remove(key)
    }
}