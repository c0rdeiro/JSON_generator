package ui.Actions

import JSONValue
import visitors.PrintJSONVisitor
import java.io.File
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.*
import java.io.IOException


class SaveToFile : Action {


    override val name: String
        get() = "Save JSON"

    override fun execute(treeItem: TreeItem) {

        var fileName = ""
        var path = ""

        val shell = Shell(Display.getDefault())
        shell.setSize(500, 500)
        shell.text = "Save JSON"
        shell.layout = FillLayout()
        val filenameLabel = Text(shell, SWT.NONE)
        filenameLabel.text = "Insert filename: "

        val filenameInput = Text(shell, SWT.BORDER)
        filenameInput.addModifyListener { event ->
            val text = event.widget as Text
            fileName = text.text

        }

        val pathLabel = Text(shell, SWT.NONE)
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

                try{
                    val newFile = File(path, fileName)
                    newFile.createNewFile()
                    newFile.writeText(visitor.output)
                    shell.dispose()
                }catch (e: IOException){
                    println("Cannot find specified path.")
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