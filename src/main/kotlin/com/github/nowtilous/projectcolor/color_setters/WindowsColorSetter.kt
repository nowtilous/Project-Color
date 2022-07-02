package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.gProjectColorMap
import com.github.nowtilous.projectcolor.utils.getForegroundColorBasedOnBrightness
import com.intellij.openapi.project.Project
import java.awt.Color
import java.awt.Component
import java.awt.Container

class WindowsColorSetter : ColorSetter() {

    override val TITLE_BAR_COMPONENT_PATH = listOf("JBLayeredPane", "MenuFrameHeader")

    override fun setTitleBar(color: Color, project: Project) {
        val titleBarComponent: Container = findTitleBarComponent(project)
        val projectLabelComponent: Component = findProjectLabelComponent(titleBarComponent)
        lockComponentColorProperty(project, titleBarComponent, "background")
        lockComponentColorProperty(project, projectLabelComponent, "foreground", true)

        // set the color
        gProjectColorMap[project] = color
        titleBarComponent.background = color
        recursiveSetComponentColor(titleBarComponent,getForegroundColorBasedOnBrightness(color), "foreground")
    }

    /**
     * Find project label component with root title bar component.
     * @throws ClassNotFoundException if none found
     */
    private fun findProjectLabelComponent(titleBarComponent: Container): Component {
        for (component in titleBarComponent.components) {
            if ("titleLabel" in component.toString()) {
                return (component as Container).getComponent(0)
            }
        }

        throw ClassNotFoundException()
    }
}