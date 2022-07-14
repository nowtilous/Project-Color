package com.github.nowtilous.projectcolor.ui

import com.github.nowtilous.projectcolor.OVERRIDE_AUTO_COLOR_CONFIG
import com.github.nowtilous.projectcolor.setTitleBarColor
import com.github.nowtilous.projectcolor.utils.getColorBasedOnProjectName
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.ProjectManager
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JCheckBox
import javax.swing.colorchooser.AbstractColorChooserPanel

const val AUTO_COLOR_SET_TOGGLED_PATH = "com.github.nowtilous.projectcolor.auto_rgb"

class ColorChooserOptionsPanel : AbstractColorChooserPanel() {

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
                    PropertiesComponent.getInstance(project).setValue(OVERRIDE_AUTO_COLOR_CONFIG, false)
                    setTitleBarColor(getColorBasedOnProjectName(project), project)
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

