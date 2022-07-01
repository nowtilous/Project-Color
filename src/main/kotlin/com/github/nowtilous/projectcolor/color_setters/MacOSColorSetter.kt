package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.utils.getForegroundColorBasedOnBrightness
import com.intellij.openapi.project.Project
import java.awt.Color
import java.awt.Container

class MacOSColorSetter : ColorSetter() {

    override val TITLE_BAR_COMPONENT_PATH = listOf("JBLayeredPane", "JBPanel", "NonOpaquePanel", "JBBox", "navbar")

    override fun setTitleBar(color: Color, project: Project) {

        val navBarComponent = findTitleBarComponent(project) as Container
        val filePathComponent = findFilePathComponent(navBarComponent)

        recursiveSetForeground(filePathComponent, getForegroundColorBasedOnBrightness(color))

        navBarComponent.background = color
        lockComponentColorProperty(project, navBarComponent, "background")
    }

    private fun findFilePathComponent(navBarComponent: Container): Container {
        for (component in navBarComponent.components) {
            for (subComponent in (component as Container).components) {
                if ("JBScrollPane" in subComponent.toString()) {
                    return component
                }
            }
        }

        throw ClassNotFoundException()
    }
}