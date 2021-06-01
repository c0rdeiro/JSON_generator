package ui.Actions

import JSONValue
import visitors.PrintJSONVisitor
import java.io.File
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.*
import utils.initializeShell
import java.io.IOException


class SaveToFile : Action {


    override val name: String
        get() = "Save JSON"

    override fun execute(treeItem: TreeItem) {

        var fileName = ""
        var path = ""
        val shell = initializeShell("Save JSON", 500, 500)

        val filenameLabel = Text(shell, SWT.READ_ONLY)
        filenameLabel.text = "Insert filename: "

        val filenameInput = Text(shell, SWT.BORDER)
        filenameInput.addModifyListener { event ->
            val text = event.widget as Text
            fileName = text.text

        }

        val pathLabel = Text(shell, SWT.READ_ONLY)
        pathLabel.text = "Insert path: "

        val pathInput = Text(shell, SWT.BORDER)
        pathInput.addModifyListener { event ->
            val text = event.widget as Text
            path = text.text

        }

        val saveButton = Button(shell, SWT.PUSH)
        saveButton.text = "Save"
        saveButton.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                val visitor = PrintJSONVisitor()
                (treeItem.data as JSONValue).accept(visitor)

                val box = MessageBox(Display.getCurrent().activeShell, SWT.OK)
                try{
                    val newFile = File(path, "$fileName.json")
                    newFile.createNewFile()
                    newFile.writeText(visitor.output)
                    box.text = "File saved."
                    box.message = "File save successfully in ${newFile.absolutePath}"
                    box.open()
                    shell.dispose()
                }catch (e: IOException){
                    box.text = "Invalid Path."
                    box.message = "Cannot find specified path."
                    box.open()
                }

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
}