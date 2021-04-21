package visitors

import JSONString

class FindAllStrings : Visitor{

    val output = mutableListOf<String>()
    val count: Int
        get(){
            return output.size
        }

    override fun visit(str: JSONString) {
        output.add(str.value)
    }

}