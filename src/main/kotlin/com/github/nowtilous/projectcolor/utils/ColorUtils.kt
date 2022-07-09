package com.github.nowtilous.projectcolor.utils

import com.intellij.openapi.project.Project
import com.intellij.ui.ColorUtil
import java.awt.Color

/**
 * Get appropriate foreground text color based on brightness of given color.
 * @return white if color is dark, and black if color is bright
 */
fun getForegroundColorBasedOnBrightness(color: Color): Color {
    return if (ColorUtil.isDark(color)) Color.white else Color.black
}

/**
 * Generate color from hash of given project's name.
 */
fun getColorBasedOnProjectName(project: Project): Color {
    return Color(project.name.hashCode())
}
