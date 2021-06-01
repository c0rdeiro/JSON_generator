package ui.Actions

import JSONNumber
import JSONValue
import models.JSONArray
import models.JSONObject
import org.eclipse.swt.widgets.TreeItem
import org.eclipse.swt.SWT


import org.eclipse.swt.widgets.MessageBox
import org.eclipse.swt.widgets.Display


//validates if all numbers are positive
class ValidateNumbers : Action {
    override val name: String
        get() = "Validate Numbers"

    override fun execute(treeItem: TreeItem) {

        val invalidCount = checkTreeData(treeItem.parent.items[0].data as JSONValue)
        val box = MessageBox(Display.getCurrent().activeShell, SWT.OK)
        box.text = "Valid Numbers"

        if (invalidCount == 0)
            box.message = "All numbers are positive."
        else
            box.message = "There "+ if (invalidCount == 1) "is" else {"are"} + " $invalidCount negative number" + if (invalidCount > 1) "s." else "."

                    box.open()

    }

    private fun checkTreeData(data: JSONValue): Int {
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