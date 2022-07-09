package com.github.nowtilous.projectcolor

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import javax.swing.*
import javax.swing.colorchooser.AbstractColorChooserPanel


class ChooseColorAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {

        val colorChooser = JColorChooser()
        val project = e.dataContext.getData(PlatformDataKeys.PROJECT) as Project
        colorChooser.selectionModel.addChangeListener {
            setTitleBarColor(colorChooser.color, project)
        }

        colorChooser.addChooserPanel(object: AbstractColorChooserPanel() {
            override fun updateChooser() {
            }

            override fun buildChooser() {
                val automaticColorSetOption = JCheckBox("Automatically set color based on project name")
                automaticColorSetOption.addActionListener{
                    val toggledOn = (it.source as JCheckBox).isSelected
                    if(toggledOn){
                        println(project.name.hashCode())
                    }
                }
                add(automaticColorSetOption)
            }

            override fun getDisplayName(): String {
                return "Options"
            }

            override fun getSmallDisplayIcon(): Icon {
                return ImageIcon()
            }

            override fun getLargeDisplayIcon(): Icon {
                return ImageIcon()
            }

        })
        val dialog = JColorChooser.createDialog(null, "Choose a title bar color", true, colorChooser, null, null)
        dialog.isVisible = true
        dialog.dispose()
    }

}


