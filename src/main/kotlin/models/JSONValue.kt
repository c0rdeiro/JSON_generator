import visitors.Visitor

abstract class JSONValue(){

    abstract fun accept(v : Visitor)

}