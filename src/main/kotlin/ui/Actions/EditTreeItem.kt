package ui.Actions

import JSONNumber
import JSONString
import JSONValue
import models.JSONArray
import models.JSONBoolean
import models.JSONKey
import models.JSONObject
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.widgets.TreeItem
import utils.initializeShell
import java.lang.IllegalArgumentException

class EditTreeItem : Action {

    override val name: String
        get() = "Edit"

    override fun execute(treeItem: TreeItem) {
        val shell = initializeShell("Edit Item", 500, 500)

        val editText = Text(shell, SWT.BORDER)
        editText.text = treeItem.text
        editText.addModifyListener { event ->
            val text = event.widget as Text
            treeItem.text = text.text

        }

        val saveButton = Button(shell, SWT.PUSH)
        saveButton.text = "Save"
        saveButton.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {

                val newValue = handleJSONData(treeItem.data as JSONValue, treeItem)

                editParentData(treeItem.parentItem, treeItem, newValue)
                treeItem.data = newValue
                //TODO: missing change of parent data and recursively until root

                shell.dispose()
            }
        })

        val cancelButton = Button(shell, SWT.PUSH)
        cancelButton.text = "Cancel"
        cancelButton.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                shell.dispose()
            }
        })

        shell.pack()
        shell.open()
    }

    private fun handleJSONData(data: JSONValue, treeItem: TreeItem): JSONValue {

        return when (data) {
            is JSONString -> JSONString(treeItem.text)
            is JSONNumber -> JSONNumber(treeItem.text.toInt())
            is JSONBoolean -> JSONBoolean(treeItem.text.toBoolean())
            else -> throw IllegalArgumentException("cannot edit that tree item node")
        }

    }

    private fun editParentData(parent: TreeItem, oldItem: TreeItem, newValue: JSONValue) {
        when (parent.data) {
            is JSONObject -> (parent.data as JSONObject).elements
                                    .forEach {
                                        if(it.value == oldItem.data)
                                                (parent.data as JSONObject).elements[it.key] = newValue
                                    }
            is JSONArray -> (parent.data as JSONArray).elements
                                    .forEach{
                                        if(it == oldItem.data)
                                            (parent.data as JSONArray).elements[(parent.data as JSONArray).elements.indexOf(it)] = newValue
                                    }
        }
    }


}