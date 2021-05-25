package ui

import JSONValue
import ui.IconSetups.IconSetup
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*
import visitors.CreateUITreeVisitor
import visitors.PrintJSONVisitor
import org.eclipse.swt.widgets.TreeItem


class Visualizer {

    val shell: Shell
    val tree: Tree

    @Inject
    private var icons: IconSetup? = null

    //@InjectAdd
    //private val actions = mutableListOf<Action>()

    init {

        val display = Display.getDefault()
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

    fun instantiate(root: JSONValue){
        Injector.create(Visualizer::class).open(root)
    }

    private fun open(root: JSONValue) {


        root.accept(CreateUITreeVisitor(tree, icons))

        tree.expandAll()
        shell.pack()
        shell.open()
        val display = Display.getDefault()
        while (!shell.isDisposed) {
            if (!display.readAndDispatch()) display.sleep()
        }
        display.dispose()
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

    private fun getAllItems(treeItem: TreeItem, allItems: MutableList<TreeItem>) {
        treeItem.items.forEach {
            allItems.add(it)
            getAllItems(it, allItems)
        }

    }

}

