package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.gProjectColorMap
import com.github.nowtilous.projectcolor.setTitleBarColor
import com.github.nowtilous.projectcolor.utils.getForegroundColorBasedOnBrightness
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.impl.IdeMenuBar
import java.awt.Color
import java.awt.Container
import java.awt.event.ContainerEvent
import java.awt.event.ContainerListener


val gProjectColorLockedMap = mutableMapOf<Project, Boolean>()

class WindowsColorSetter : ColorSetter() {

    override val TITLE_BAR_COMPONENT_PATH = listOf("JBLayeredPane", "MenuFrameHeader")

    override fun setTitleBar(color: Color, project: Project) {
        val titleBarComponent: Container = findTitleBarComponent(project)
        lockComponentColorProperty(project, titleBarComponent, "both", true)
        lockContainerColor(findIdeMenuBar(titleBarComponent), project)

        // set the color
        titleBarComponent.background = color
        recursiveSetComponentColor(titleBarComponent, getForegroundColorBasedOnBrightness(color), "foreground")
    }

    /**
     * Lock color properties for given container of components.
     *
     * @param container the container to lock changes to.
     * @param project the project to which the container belongs to.
     *
     * @note For some reason, Swing (or Intellij) completely swap out some container's components
     *       when rebuilding the UI, so the color locking trick I did earlier is not enough,
     *       since it will only lock components which might get disposed of some day...
     *       Hence, this function is introduced, which adds a listener to container change events
     *       such as component added, component removed, etc.
     *       Once such event occurs, the title bar color will be reset.
     */
    private fun lockContainerColor(container: Container, project: Project) {
        if (!gProjectColorLockedMap.containsKey(project) || gProjectColorLockedMap[project] == false) {
            container.addContainerListener(object : ContainerListener {
                override fun componentAdded(e: ContainerEvent?) {
                    gProjectColorMap[project]?.let { setTitleBarColor(it, project) }
                }

                override fun componentRemoved(e: ContainerEvent?) {
                    gProjectColorMap[project]?.let { setTitleBarColor(it, project) }
                }
            })
            gProjectColorLockedMap[project] = true
        }
    }

    private fun findIdeMenuBar(titleBarComponent: Container): IdeMenuBar {
        for (comp in titleBarComponent.components) {
            if ("JPanel" in comp.toString()) {
                for (subComp in (comp as Container).components) {
                    if ("IdeMenuBar" in subComp.toString()) {
                        return subComp as IdeMenuBar
                    }
                }
            }
        }
        throw ClassNotFoundException()
    }
}