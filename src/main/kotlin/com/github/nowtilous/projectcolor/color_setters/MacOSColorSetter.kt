package com.github.nowtilous.projectcolor.color_setters

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.ColorUtil
import java.awt.Color
import java.awt.Component
import java.awt.Container
import javax.swing.JFrame

class MacOSColorSetter : ColorSetter() {

    override fun setTitleBar(color: Color, project: Project) {

        val navBarComponent = findTitleBarComponent(project) as Container

        recursiveSetForeground(
            findFilePathComponent(navBarComponent),
            if (ColorUtil.isDark(color)) Color.white else Color.black
        )

        navBarComponent.background = color
        lockComponentColorProperty(project, navBarComponent, "background")
    }

    override fun findTitleBarComponent(project: Project): Component {
        val mainIdeComponent = (WindowManager.getInstance().getFrame(project) as JFrame).getComponent(0) as Container
        var mainComponentPane: Container? = null

        for (component in mainIdeComponent.components) {
            if ("JBLayeredPane" in component.toString()) {
                mainComponentPane = component as Container
                break
            }
        }

        for (component in (mainComponentPane as Container).components) {
            if ("JBPanel" in component.toString()) {
                mainComponentPane = component as Container
                break
            }
        }

        for (component in (mainComponentPane as Container).components) {
            if ("NonOpaquePanel" in component.toString()) {
                mainComponentPane = component as Container
                break
            }
        }

        for (component in (mainComponentPane as Container).components) {
            if ("JBBox" in component.toString()) {
                mainComponentPane = component as Container
                break
            }
        }

        for (component in (mainComponentPane as Container).components) {
            if ("navbar" in component.toString()) {
                return component
            }
        }

        throw ClassNotFoundException()
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