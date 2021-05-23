package visitors

import JSONNumber
import JSONString
import JSONValue
import UI.IconSetup
import models.*
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem
import java.lang.IllegalArgumentException

class CreateUITree(val tree: Tree, val icons: IconSetup?) : Visitor {


    private var currParent: TreeItem? = null
    private var currKey: String = ""


    override fun visit(str: JSONString) {
        val node = TreeItem(currParent, SWT.NONE)

        populateNode(node, str)
    }


    override fun visit(number: JSONNumber) {

        val node = TreeItem(currParent, SWT.NONE)

        populateNode(node, number)
    }


    override fun visit(n: JSONNull) {

        val node = TreeItem(currParent, SWT.NONE)

        populateNode(node, n)
    }


    override fun visit(bool: JSONBoolean) {

        val node = TreeItem(currParent, SWT.NONE)

        populateNode(node, bool)
    }

    override fun visit(key: JSONKey) {
        currKey = "${key.value}: "
    }


    override fun visit(obj: JSONObject): Boolean {
        val node: TreeItem = if (currParent == null)
            TreeItem(tree, SWT.NONE)
        else
            TreeItem(currParent, SWT.NONE)

        populateNode(node, obj)
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

        populateNode(node, arr)
        currParent = node

        currKey = ""

        return true
    }

    override fun endVisit(arr: JSONArray) {
        currParent = currParent?.parentItem
    }

    fun populateNode(node: TreeItem, value: JSONValue){


        if(icons != null){
            node.text = icons.getText(value)
            node.image = Image(Display.getDefault(), icons.getPath(value))
        }
        else{
            node.text = currKey + valueToString(value)
        }
        node.data = value

        currKey = ""
    }

    fun valueToString(value: JSONValue): String{

        return when(value){

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