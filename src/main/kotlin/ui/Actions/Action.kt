package ui.Actions

import org.eclipse.swt.widgets.TreeItem

interface Action {
    val name: String
    fun execute(treeItem: TreeItem)
}
