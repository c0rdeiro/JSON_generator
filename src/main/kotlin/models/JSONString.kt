import visitors.Visitor

open class JSONString(val value: String) : JSONValue() {


    override fun accept(v: Visitor) {
        v.visit(this)
    }

}