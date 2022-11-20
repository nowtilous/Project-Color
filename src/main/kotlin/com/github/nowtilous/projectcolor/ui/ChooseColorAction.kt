package com.github.nowtilous.projectcolor.ui

import com.github.nowtilous.projectcolor.COLOR_SETTING_PATH
import com.github.nowtilous.projectcolor.setTitleBarColor
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.event.ActionListener
import javax.swing.JColorChooser

class ChooseColorAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.dataContext.getData(PlatformDataKeys.PROJECT) as Project
        val currentColor = PropertiesComponent.getInstance(project).getInt(COLOR_SETTING_PATH, getThemeBackgroundColor(e))

        val colorChooser = JColorChooser(Color(currentColor))
        colorChooser.selectionModel.addChangeListener {
            setTitleBarColor(colorChooser.color, project)
        }

        val cancelListener = ActionListener {
            colorChooser.setColor(currentColor)
        }

        colorChooser.addChooserPanel(ColorChooserOptionsPanel())
        val dialog = JColorChooser.createDialog(null, "Choose a title bar color", true, colorChooser, null, cancelListener)
        dialog.isVisible = true
        dialog.dispose()
    }

    private fun getThemeBackgroundColor(e: AnActionEvent) : Int {
        val background: JBColor = e.dataContext.getData(PlatformDataKeys.CONTEXT_COMPONENT)?.background as JBColor
        return background.rgb
    }
}
