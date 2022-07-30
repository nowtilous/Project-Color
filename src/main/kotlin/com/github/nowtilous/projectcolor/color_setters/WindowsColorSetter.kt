package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.gColorLockedComponentMap
import com.github.nowtilous.projectcolor.utils.getForegroundColorBasedOnBrightness
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.impl.IdeMenuBar
import java.awt.Color
import java.awt.Container

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

    override fun cleanUp(project: Project) {
        unlockComponentsColors(findTitleBarComponent(project))
    }

    private fun unlockComponentsColors(container: Container){
        for (component in container.components){
            gColorLockedComponentMap.remove(component)
            unlockComponentsColors(component as Container)
        }
    }
}