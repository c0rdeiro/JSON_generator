package ui.Actions

import JSONNumber
import JSONString
import JSONValue
import models.JSONBoolean
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.widgets.TreeItem
import utils.initializeShell

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

                handleJSONData(treeItem.data as JSONValue, treeItem)

                //editParentData(treeItem.parentItem, treeItem)
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

    private fun handleJSONData(data: JSONValue, treeItem: TreeItem) {

        when(data){
            is JSONString -> treeItem.data = JSONString(treeItem.text)
            is JSONNumber -> treeItem.data = JSONNumber(treeItem.text.toInt())
            is JSONBoolean -> treeItem.data = treeItem.text.toBoolean()
        }

    }

//    private fun editParentData(parent: TreeItem, currentItem: TreeItem){
//
//        parent.data = currentItem.data //só é verdade para os seguintes, no primeiro tem de ir buscar o resto dos items
//    }


}