package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.utils.getForegroundColorBasedOnBrightness
import com.intellij.openapi.project.Project
import java.awt.Color

class LinuxColorSetter : ColorSetter() {
    override val TITLE_BAR_COMPONENT_PATH = listOf("JBLayeredPane", "LinuxIdeMenuBar")

    override fun setTitleBar(color: Color, project: Project) {
        val titleBarComponent = findTitleBarComponent(project)

        recursiveSetComponentColor(titleBarComponent, color, "background")
        recursiveSetComponentColor(titleBarComponent, getForegroundColorBasedOnBrightness(color), "foreground")
    }
}