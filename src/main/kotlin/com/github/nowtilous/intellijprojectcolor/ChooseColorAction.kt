package com.github.nowtilous.intellijprojectcolor

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataConstants
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import javax.swing.JColorChooser
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.event.ChangeEvent
import javax.swing.plaf.ColorUIResource


class ChooseColorAction : AnAction() {

    private val mChooser = JColorChooser()
    private lateinit var mActionEvent: AnActionEvent
    override fun actionPerformed(e: AnActionEvent) {
        mActionEvent = e
        mChooser.selectionModel.addChangeListener { changeEvent ->
            setTitleBarColor(changeEvent)
        }
        val dialog = JColorChooser.createDialog(null, "Choose a title bar color", true, mChooser, null, null)
        dialog.isVisible = true
    }
    private fun setTitleBarColor(e: ChangeEvent?){
        UIManager.put("TitlePane.background", ColorUIResource(mChooser.color))

        val project = mActionEvent.dataContext.getData(PlatformDataKeys.PROJECT)
        SwingUtilities.updateComponentTreeUI(WindowManager.getInstance().getFrame(project))
        println(mChooser.color)
    }
}

