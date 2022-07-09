package com.github.nowtilous.projectcolor

import com.github.nowtilous.projectcolor.color_setters.ColorSetterFactory
import com.github.nowtilous.projectcolor.ui.AUTO_COLOR_SET_TOGGLED_PATH
import com.github.nowtilous.projectcolor.utils.getColorBasedOnProjectName
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import java.awt.Color
import java.awt.Component

const val COLOR_SETTING_PATH = "com.github.nowtilous.projectcolor.rgb"
const val OVERRIDE_AUTO_COLOR_CONFIG = "com.github.nowtilous.projectcolor.override_auto_rgb"
val gColorLockedComponentMap = mutableMapOf<Component, Boolean>()
val gProjectColorMap = mutableMapOf<Project, Color>()

/**
 * Set title bar color with given color for given project.
 */
fun setTitleBarColor(color: Color, project: Project) {

    gProjectColorMap[project] = color
    ColorSetterFactory.getColorSetter().setTitleBar(color, project)

    if(PropertiesComponent.getInstance().getBoolean(AUTO_COLOR_SET_TOGGLED_PATH)
        && color != getColorBasedOnProjectName(project)){
        PropertiesComponent.getInstance(project).setValue(OVERRIDE_AUTO_COLOR_CONFIG, true)
    }
    // save color config for persistence
    PropertiesComponent.getInstance(project).setValue(COLOR_SETTING_PATH, color.rgb, 0)
}
