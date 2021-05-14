package visitors

import JSONNumber
import JSONString
import models.*
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem

class CreateUITree(val tree: Tree) : Visitor {

    var currParent: TreeItem? = null
    var currKey: String = ""

    override fun visit(str: JSONString) {
        val node = TreeItem(currParent, SWT.NONE)

        node.text = currKey + "\"${str.value}\""
        node.data = str

        currKey = ""
    }

    override fun visit(number: JSONNumber) {

        val node = TreeItem(currParent, SWT.NONE)
        node.text = currKey + number.value.toString()
        node.data = number

        currKey = ""
    }

    override fun visit(n: JSONNull) {

        val node = TreeItem(currParent, SWT.NONE)
        node.text = currKey + "null"
        node.data = n

        currKey = ""
    }

    override fun visit(bool: JSONBoolean) {

        val node = TreeItem(currParent, SWT.NONE)
        node.text = currKey + bool.value.toString()
        node.data = bool

        currKey = ""
    }

    override fun visit(key: JSONKey) {
        currKey = "${key.value}: "
    }

    override fun visit(obj: JSONObject): Boolean {
        val node: TreeItem = if (currParent == null)
            TreeItem(tree, SWT.NONE)
        else
            TreeItem(currParent, SWT.NONE)

        node.text = "(object)"
        node.data = obj
        currParent = node
        return true
    }

    override fun endVisit(obj: JSONObject) {
        currParent = currParent?.parentItem
    }

    override fun visit(arr: JSONArray): Boolean {
        val node: TreeItem = if (currParent == null)
            TreeItem(tree, SWT.NONE)
        else
            TreeItem(currParent, SWT.NONE)

        node.text = if (currKey.isEmpty()) "(array)" else currKey

        node.data = arr
        currParent = node


        currKey = ""

        return true
    }

    override fun endVisit(arr: JSONArray) {
        currParent = currParent?.parentItem
    }

}