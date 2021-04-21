import visitors.Visitor

class JSONNumber(val value: Number) : JSONValue() {

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}