package ui.Actions

import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.widgets.TreeItem

class EditTreeItem : Action {

    override val name: String
        get() = "Edit"

    override fun execute(treeItem: TreeItem) {
        val shell = Shell(Display.getCurrent())

    }


}