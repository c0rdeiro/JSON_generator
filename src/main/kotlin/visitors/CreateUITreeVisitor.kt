package visitors

import JSONNumber
import JSONString
import JSONValue
import ui.IconSetups.IconSetup
import models.*
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem
import java.lang.IllegalArgumentException
import kotlin.test.currentStackTrace

class CreateUITreeVisitor(val tree: Tree, val icons: IconSetup?) : Visitor {


    private var currParent: TreeItem? = null
    private var currKey: String = ""


    override fun visit(str: JSONString) {
        if (icons != null && icons.toExclude(str)) {
            if (currParent != null)
                removeValFromParent(str)
        } else {
            val node = TreeItem(currParent, SWT.NONE)
            populateNode(node, str)
        }
    }

    override fun visit(number: JSONNumber) {
        if (icons != null && icons.toExclude(number)) {
            if (currParent != null)
                removeValFromParent(number)
        } else {
            val node = TreeItem(currParent, SWT.NONE)
            populateNode(node, number)
        }
    }

    override fun visit(n: JSONNull) {
        if (icons != null && icons.toExclude(n)) {
            if (currParent != null)
                removeValFromParent(n)
        } else {
            val node = TreeItem(currParent, SWT.NONE)
            populateNode(node, n)
        }
    }

    override fun visit(bool: JSONBoolean) {
        if (icons != null && icons.toExclude(bool)) {
            if (currParent != null)
                removeValFromParent(bool)
        } else {
            val node = TreeItem(currParent, SWT.NONE)
            populateNode(node, bool)
        }
    }

    override fun visit(key: JSONKey) {
        currKey = "${key.value}: "
    }

    override fun visit(obj: JSONObject): Boolean {
        if (icons != null && icons.toExclude(obj)) {
            if (currParent != null)
                removeValFromParent(obj)
            return false
        }
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
        if (icons != null && icons.toExclude(arr)) {
            if (currParent != null)
                removeValFromParent(arr)
            return false
        }

        val node: TreeItem = if (currParent == null)
            TreeItem(tree, SWT.NONE)
        else
            TreeItem(currParent, SWT.NONE)

        populateNode(node, arr)
        currParent = node

        return true
    }

    override fun endVisit(arr: JSONArray) {
        currParent = currParent?.parentItem
    }

    private fun populateNode(node: TreeItem, value: JSONValue) {

        if (icons != null) {
            node.text = icons.getText(value)
            val img = Image(Display.getDefault(), icons.getPath(value))
            node.image = Image(
                Display.getDefault(),
                img.imageData.scaledTo(icons.getIconWidth(value), icons.getIconHeight(value))
            )
        } else {
            node.text = currKey + valueToString(value)
        }
        node.data = value

        currKey = ""
    }


    private fun removeValFromParent(value: JSONValue) {
        val oldValue: JSONValue = currParent!!.data as JSONValue
        when (currParent!!.data) {
            is JSONObject ->
                currParent!!.data =
                    JSONObject((currParent!!.data as JSONObject).elements.filterValues { it != value } as MutableMap<JSONKey, JSONValue>)
            is JSONArray ->
                currParent!!.data =
                    JSONArray((currParent!!.data as JSONArray).elements.filter { it != value } as MutableList<JSONValue>)
        }
        if (currParent!!.parentItem != null)
            deleteUntilRoot(oldValue, currParent!!.data as JSONValue, currParent!!.parentItem)
    }

    private fun deleteUntilRoot(oldValue: JSONValue, newValue: JSONValue, parent: TreeItem) {
        val new_oldValue: JSONValue = parent.data as JSONValue
        when (parent.data) {
            is JSONObject ->

                (parent.data as JSONObject).elements
                    .forEach { if (oldValue == it.value) (parent.data as JSONObject).elements[it.key] = newValue }
            is JSONArray ->
                (parent.data as JSONArray).elements
                    .forEach {
                        if (it == oldValue) (parent.data as JSONArray).elements[(parent.data as JSONArray).elements.indexOf(
                            it
                        )] = newValue
                    }

        }
        if (parent.parentItem != null)
            deleteUntilRoot(new_oldValue, parent.data as JSONValue, parent.parentItem)
    }

    private fun valueToString(value: JSONValue): String {

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