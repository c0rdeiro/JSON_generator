package UI

import JSONGenerator
import JSONValue
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*
import visitors.CreateUITree
import visitors.PrintJSONVisitor
import org.eclipse.swt.widgets.TreeItem





fun main() {
    data class Point(val x: Int, val y: Int)
    data class Person(
        val name: String,
        val age: Int,
        val location: Point,
        val pepe: List<String>,
        val hist: List<Point>
    )

    val arr = listOf<String>("potato", "poteto")
    val arrP = listOf<Point>(Point(1, 1), Point(5, 5))
    val test = Person("Francisco", 21, Point(2, 2), arr, arrP)


    val o = JSONGenerator().instantiate(test)
    Structure().open(o)

}

class Structure() {


    val shell: Shell
    val tree: Tree

    init {
        shell = Shell(Display.getDefault())
        shell.setSize(1000, 1000)
        shell.text = "File tree skeleton"
        shell.layout = GridLayout(2, false)

        tree = Tree(shell, SWT.SINGLE or SWT.BORDER)
        val label = Label(shell, SWT.NONE)

        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                val visitor = PrintJSONVisitor()
                val item = tree.selection.first()
                (item.data as JSONValue).accept(visitor)
                label.text = visitor.output

                shell.setSize(1000, 500) //TODO: change this to set window size
                shell.layout(true) //update layout

            }
        })


        val searchInput = Text(shell, SWT.BORDER)

        searchInput.addModifyListener { event -> // Get the widget whose text was modified
            val text = event.widget as Text

            getAllItems(tree)
                .forEach {
                    if (it.text.contains(text.text) && text.text.isNotBlank())
                        it.setBackground(Color(255, 255, 102))
                    else
                        it.setBackground(null)

                }


        }


    }

    // auxiliar para profundidade do nó
    fun TreeItem.depth(): Int =
        if (parentItem == null) 0
        else 1 + parentItem.depth()


    fun open(root: JSONValue) {
        root.accept(CreateUITree(tree))

        tree.expandAll()
        shell.pack()
        shell.open()
        val display = Display.getDefault()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        display.dispose()
    }
}

private fun Tree.expandAll() = traverse { it.expanded = true }

private fun Tree.traverse(visitor: (TreeItem) -> Unit) {
    fun TreeItem.traverse() {
        visitor(this)
        items.forEach {
            it.traverse()
        }
    }
    items.forEach { it.traverse() }
}

private fun getAllItems(tree: Tree) : MutableList<TreeItem>{
    val allItems = mutableListOf<TreeItem>()

    for (item in tree.items)
        getAllItems(item, allItems)

    return allItems
}

private fun getAllItems(currentItem: TreeItem, allItems: MutableList<TreeItem>) {

    val children = currentItem.items

    for (child in children) {
        allItems.add(child)
        getAllItems(child, allItems)
    }
}
