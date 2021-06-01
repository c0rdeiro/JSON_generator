import visitors.Visitor

class JSONNumber(var value: Number) : JSONValue() {

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}