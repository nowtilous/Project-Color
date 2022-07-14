package com.github.nowtilous.projectcolor.ui

import com.github.nowtilous.projectcolor.setTitleBarColor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import javax.swing.JColorChooser


class ChooseColorAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {

        val colorChooser = JColorChooser()
        val project = e.dataContext.getData(PlatformDataKeys.PROJECT) as Project
        colorChooser.selectionModel.addChangeListener {
            setTitleBarColor(colorChooser.color, project)
        }

        colorChooser.addChooserPanel(ColorChooserOptionsPanel())
        val dialog = JColorChooser.createDialog(null, "Choose a title bar color", true, colorChooser, null, null)
        dialog.isVisible = true
        dialog.dispose()
    }

}
