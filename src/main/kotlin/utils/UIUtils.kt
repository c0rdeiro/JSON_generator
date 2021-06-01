package utils

import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell


 internal fun initializeShell(shellName: String, width: Int, height: Int): Shell{

    val shell = Shell(Display.getDefault())
    shell.setSize(width, height)
    shell.text = shellName
    shell.layout = FillLayout()

    return shell
}