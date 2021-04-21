package models
import JSONValue
import visitors.Visitor

class JSONObject(val elems : MutableMap<JSONKey, JSONValue?>? = null) : JSONValue() {



    val nProperties: Int
        get() {

            if(elems.isNullOrEmpty())
                return 0
            else
                return elems.keys.size

        }

    val nElements: Int
        get(){
            var count = 0

            if(!elems.isNullOrEmpty())
                this.elems.forEach {
                    if(it.value is JSONObject)
                        count += (it.value as JSONObject).nElements
                    else
                        count++
                }

            return count
        }

    override fun accept(v: Visitor) {
        if (!elems.isNullOrEmpty())

            if (v.visit(this)) {
                elems.forEach {

                    v.visit(it.key)
                    if(it.value == null)
                        v.visit(JSONNull())
                    else
                        it.value!!.accept(v)
                }

                v.endVisit(this)
            }

    }
}