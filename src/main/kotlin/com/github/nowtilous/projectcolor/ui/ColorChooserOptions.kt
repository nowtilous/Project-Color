package com.github.nowtilous.projectcolor.ui

import com.github.nowtilous.projectcolor.setTitleBarColor
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.ProjectManager
import java.awt.Color
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JCheckBox
import javax.swing.colorchooser.AbstractColorChooserPanel

class ColorChooserOptionsPanel : AbstractColorChooserPanel() {

    private val AUTO_COLOR_SET_TOGGLED_PATH = "com.github.nowtilous.projectcolor.auto_rgb"

    override fun updateChooser() {
    }

    override fun buildChooser() {
        val automaticColorSetOption = JCheckBox("Automatically set color based on project name")
        val isToggledOn = PropertiesComponent.getInstance().getBoolean(AUTO_COLOR_SET_TOGGLED_PATH)
        automaticColorSetOption.isSelected = isToggledOn

        automaticColorSetOption.addActionListener {
            val toggledOn = (it.source as JCheckBox).isSelected
            if (toggledOn) {
                for (project in ProjectManager.getInstance().openProjects) {
                    setTitleBarColor(Color(project.name.hashCode()), project)
                }
            }

            PropertiesComponent.getInstance().setValue(AUTO_COLOR_SET_TOGGLED_PATH, toggledOn)
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
}

