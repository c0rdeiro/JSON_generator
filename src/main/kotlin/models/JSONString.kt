import visitors.Visitor

open class JSONString(var value: String) : JSONValue() {


    override fun accept(v: Visitor) {
        v.visit(this)
    }

}