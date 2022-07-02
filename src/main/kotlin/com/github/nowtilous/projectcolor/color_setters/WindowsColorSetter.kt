package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.gProjectColorMap
import com.github.nowtilous.projectcolor.utils.getForegroundColorBasedOnBrightness
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import java.awt.Color
import java.awt.Component
import java.awt.Container
import javax.swing.JFrame

class WindowsColorSetter : ColorSetter() {

    override val TITLE_BAR_COMPONENT_PATH = listOf("JBLayeredPane", "MenuFrameHeader")

    override fun setTitleBar(color: Color, project: Project) {
        val titleBarComponent: Container = findTitleBarComponent(project)
        val projectLabelComponent: Component = findProjectLabelComponent(titleBarComponent)
        lockComponentColorProperty(project, titleBarComponent, "background")
        lockComponentColorProperty(project, projectLabelComponent, "foreground")

        // set the color
        gProjectColorMap[project] = color
        titleBarComponent.background = color
        recursiveSetComponentColor(titleBarComponent,getForegroundColorBasedOnBrightness(color), "foreground")
    }


    /**
     * Find title bar java component for given project.
     *
     * @note searches using java swing object names, which might change in future versions.
     * @throws ClassNotFoundException if none found
     * @returns the component if found, null otherwise.
     */
    override fun findTitleBarComponent(project: Project): Container {
        val mainIdeComponent = (WindowManager.getInstance().getFrame(project) as JFrame).getComponent(0) as Container
        var mainComponentPane: Container? = null

        for (component in mainIdeComponent.components) {
            if ("JBLayeredPane" in component.toString()) {
                mainComponentPane = component as Container
                break
            }
        }

        if (mainComponentPane != null) {
            for (component in mainComponentPane.components) {
                if ("MenuFrameHeader" in component.toString()) {
                    return component as Container
                }
            }
        }

        throw ClassNotFoundException()
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