package com.github.nowtilous.intellijprojectcolor

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import javax.swing.JColorChooser
import javax.swing.UIManager
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import javax.swing.plaf.ColorUIResource


class ChooseColorAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        UIManager.put("TitlePane.background", ColorUIResource(0, 0, 0))

        val chooser = JColorChooser()
        chooser.selectionModel.addChangeListener { changeEvent ->
            setTitleBarColor(changeEvent)
        }
        val dialog = JColorChooser.createDialog(null, "Choose a title bar color", true, chooser, null, null)
        dialog.isVisible = true
    }
    private fun setTitleBarColor(e: ChangeEvent?){
        println(e)
    }
}

