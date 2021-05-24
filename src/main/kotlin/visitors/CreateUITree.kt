package visitors

import JSONNumber
import JSONString
import JSONValue
import UI.Setups.IconSetup
import models.*
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem
import java.lang.IllegalArgumentException

class CreateUITree(val tree: Tree, val icons: IconSetup) : Visitor {


    private var currParent: TreeItem? = null
    private var currKey: String = ""


    override fun visit(str: JSONString) {

        if (!icons.toExclude(str)) {
            val node = TreeItem(currParent, SWT.NONE)
            populateNode(node, str)
        }
    }


    override fun visit(number: JSONNumber) {

        if (!icons.toExclude(number)) {
            val node = TreeItem(currParent, SWT.NONE)
            populateNode(node, number)
        }
    }


    override fun visit(n: JSONNull) {

        if (!icons.toExclude(n)) {
            val node = TreeItem(currParent, SWT.NONE)
            populateNode(node, n)
        }
    }


    override fun visit(bool: JSONBoolean) {

        if (!icons.toExclude(bool)) {
            val node = TreeItem(currParent, SWT.NONE)
            populateNode(node, bool)
        }
    }

    override fun visit(key: JSONKey) {
        currKey = "${key.value}: "
    }


    override fun visit(obj: JSONObject): Boolean {

        if(!icons.toExclude(obj)) {
            val node: TreeItem = if (currParent == null)
                TreeItem(tree, SWT.NONE)
            else
                TreeItem(currParent, SWT.NONE)

            populateNode(node, obj)
            currParent = node

            return true
        }
        return false
    }

    override fun endVisit(obj: JSONObject) {
        currParent = currParent?.parentItem
    }


    override fun visit(arr: JSONArray): Boolean {
        if(!icons.toExclude(arr)) {
            val node: TreeItem = if (currParent == null)
                TreeItem(tree, SWT.NONE)
            else
                TreeItem(currParent, SWT.NONE)

            populateNode(node, arr)
            currParent = node
            return true
        }

        return false
    }

    override fun endVisit(arr: JSONArray) {
        currParent = currParent?.parentItem
    }

    fun populateNode(node: TreeItem, value: JSONValue) {


        if (icons != null) {
            node.text = icons.getText(value)
            node.image = Image(Display.getDefault(), icons.getPath(value))
        } else {
            node.text = currKey + valueToString(value)
        }
        node.data = value

        currKey = ""
    }

    fun valueToString(value: JSONValue): String {

        return when (value) {

            is JSONString -> "\"${value.value}\""
            is JSONNumber -> value.value.toString()
            is JSONBoolean -> value.value.toString()
            is JSONNull -> "null"
            is JSONObject -> "(object)"
            is JSONArray -> "(array)"
            else -> throw IllegalArgumentException("Unsupported data type.")

        }
    }

}