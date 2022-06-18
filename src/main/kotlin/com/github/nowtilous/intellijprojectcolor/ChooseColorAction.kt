package com.github.nowtilous.intellijprojectcolor

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import java.awt.Color
import java.awt.Component
import java.awt.Container
import javax.swing.JColorChooser
import javax.swing.JFrame

const val COLOR_SETTING_PATH = "com.github.nowtilous.intellijprojectcolor.rgb"

val gPatchedComponentMap = mutableMapOf<Component, Boolean>()
val gProjectColorMap = mutableMapOf<Project, Color>()

class ChooseColorAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val colorChooser = JColorChooser()
        colorChooser.selectionModel.addChangeListener {
            setTitleBarColor(colorChooser.color, e.dataContext.getData(PlatformDataKeys.PROJECT) as Project)
        }
        val dialog = JColorChooser.createDialog(null, "Choose a title bar color", true, colorChooser, null, null)
        dialog.isVisible = true
    }

}

/**
 * Set title bar color with given color for given project.
 */
fun setTitleBarColor(color: Color, project: Project) {

    val titleBarComponent: Component = findTitleBarComponent(project) ?: return
    disableExternalChangesToComponentBackground(titleBarComponent, project)

    // set the color
    gProjectColorMap[project] = color
    titleBarComponent.background = color

    // save color config for persistence
    PropertiesComponent.getInstance(project).setValue(COLOR_SETTING_PATH, color.rgb, 0)
}

/**
 * Disable any unintended changes to given component of a project from external sources.
 *
 * @param component: component to lock changes to.
 * @param project: the project to which the component belongs to.
 *
 * @note registers a PropertyChangeListener for given property name which `aggressively`
 * reverts the color back to the one set by this plugin.
 */
fun disableExternalChangesToComponentBackground(component: Component, project: Project) {
    if (!gPatchedComponentMap.containsKey(component)) {
        gPatchedComponentMap[component] = false
    }

    if (gPatchedComponentMap[component] == false) {
        component.addPropertyChangeListener("background") {
            if (it.newValue != (gProjectColorMap[project] as Color).rgb) {
                component.background = gProjectColorMap[project]
            }
        }

        gPatchedComponentMap[component] = true
    }
}

/**
 * Find title bar java component for given project.
 *
 * @note searches using java swing object names, which might change in future versions.
 * @returns the component if found, null otherwise.
 */
fun findTitleBarComponent(project: Project): Component? {
    val mainIdeComponent = (WindowManager.getInstance().getFrame(project) as JFrame).getComponent(0) as Container
    var mainComponentPane: Container? = null
    var titleBarComponent: Component? = null

    for (component in mainIdeComponent.components) {
        if ("JBLayeredPane" in component.toString()) {
            mainComponentPane = component as Container
        }
    }

    if (mainComponentPane != null) {
        for (component in mainComponentPane.components) {
            if ("MenuFrameHeader" in component.toString()) {
                titleBarComponent = component
            }
        }
    }

    return titleBarComponent
}