package UI

import JSONGenerator
import JSONValue
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*
import visitors.CreateUITree
import visitors.PrintJSONVisitor


fun main() {
    data class Point (val x :Int, val y :Int)
    data class Person (val name: String, val age: Int, val location: Point, val pepe: List<String>, val hist: List<Point>)

    val arr = listOf<String>("potato", "poteto")
    val arrP = listOf<Point>(Point(1, 1), Point(5,5))
    val test = Person("Francisco", 21, Point(2, 2),arr, arrP )


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
        shell.layout = GridLayout(2,false)

        tree = Tree(shell, SWT.SINGLE or SWT.BORDER)
        val label = Label(shell, SWT.NONE)

        tree.addSelectionListener(object : SelectionAdapter() {
            override fun widgetSelected(e: SelectionEvent) {
                //TODO: instanciate new window with PrintJSON visitor
                val visitor = PrintJSONVisitor()
                val item = tree.selection.first()
                (item.data as JSONValue).accept(visitor)
                label.text = visitor.output

                shell.layout(true) //update layout

            }
        })


    }

    // auxiliar para profundidade do nó
    fun TreeItem.depth(): Int =
        if(parentItem == null) 0
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

fun Tree.expandAll() = traverse { it.expanded = true }

fun Tree.traverse(visitor: (TreeItem) -> Unit) {
    fun TreeItem.traverse() {
        visitor(this)
        items.forEach {
            it.traverse()
        }
    }
    items.forEach { it.traverse() }
}


