package com.github.nowtilous.projectcolor

import com.github.nowtilous.projectcolor.color_setters.ColorSetterFactory
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import java.awt.Color
import java.awt.Component

const val COLOR_SETTING_PATH = "com.github.nowtilous.projectcolor.rgb"

val gColorLockedComponentMap = mutableMapOf<Component, Boolean>()
val gProjectColorMap = mutableMapOf<Project, Color>()

/**
 * Set title bar color with given color for given project.
 */
fun setTitleBarColor(color: Color, project: Project) {

    ColorSetterFactory.getColorSetter().setTitleBar(color, project)
    // save color config for persistence
    PropertiesComponent.getInstance(project).setValue(COLOR_SETTING_PATH, color.rgb, 0)
}
