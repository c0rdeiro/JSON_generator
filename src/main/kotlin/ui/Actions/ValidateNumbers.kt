package ui.Actions

import JSONNumber
import JSONValue
import models.JSONArray
import models.JSONObject
import org.eclipse.swt.widgets.TreeItem


//validates if all numbers are positive
class ValidateNumbers : Action {
    override val name: String
        get() = "Validate Numbers"

    override fun execute(treeItem: TreeItem) {

        val invalidCount = handleTreeData(treeItem.parent.items[0].data as JSONValue)

        if (invalidCount == 0) println("valid boy")
        else println("no bueno  $invalidCount")


    }

    private fun handleTreeData(data: JSONValue): Int {
        var count = 0
        fun aux(data: JSONValue) {
            when (data) {
                is JSONNumber -> if (data.value.toInt() < 0) count++
                is JSONArray -> data.elements.forEach { aux(it) }
                is JSONObject -> data.elements.forEach { aux(it.value) }
            }
        }
        aux(data)
        return count
    }
}