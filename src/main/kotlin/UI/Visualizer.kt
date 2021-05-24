package UI

import JSONGenerator
import JSONValue
import UI.Setups.IconSetup
import models.JSONNull
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
    data class Point(val x: Int, val y: Int )
    data class Person(
        val name: String,
        val age: Int,
        val location: Point,
        val pepe: List<String>,
        val hist: List<Point>
    )

    val arr = listOf<String>("potato", "poteto", "radon")
    val arrP = listOf<Point>(Point(1, 1), Point(5, 5))
    val test = Person("Francisco", 21, Point(2, 2), arr, arrP)


    val o = JSONGenerator().instantiate(test)
    //Visualizer().open(o)
    val v = Injector.create(Visualizer::class)
    v.open(o)

}

class Visualizer() {

    val shell: Shell
    val tree: Tree
    var display: Display

    @Inject
    private lateinit var icons: IconSetup

    init {

        display = Display.getDefault()
        shell = Shell(display)
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

                shell.setSize(1000, 500) //TODO: change this to dynamic fit
                shell.layout(true) //update layout

            }
        })

        val searchInput = Text(shell, SWT.BORDER)

        searchInput.addModifyListener { event ->
            val text = event.widget as Text

            getAllItems(tree)
                .forEach {
                    if (it.text.contains(text.text) && text.text.isNotBlank())
                        it.background = Color(255, 255, 102)
                    else
                        it.background = null
                }
        }
    }

    fun open(root: JSONValue) {


        root.accept(CreateUITree(tree, icons))

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

private fun getAllItems(tree: Tree): MutableList<TreeItem> {
    val allItems = mutableListOf<TreeItem>()

    tree.items.forEach {
        getAllItems(it, allItems)
    }

    return allItems
}
fun getAllItems(treeItem: TreeItem, allItems: MutableList<TreeItem>){
    treeItem.items.forEach {
        allItems.add(it)
        getAllItems(it, allItems)
    }

}
