package com.github.nowtilous.projectcolor

import com.google.zxing.NotFoundException
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.ColorUtil
import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.security.InvalidParameterException
import javax.swing.JColorChooser
import javax.swing.JFrame

const val COLOR_SETTING_PATH = "com.github.nowtilous.projectcolor.rgb"

val gColorLockedComponentMap = mutableMapOf<Component, Boolean>()
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

    val titleBarComponent: Container = findTitleBarComponent(project) as Container
    val projectLabelComponent: Component = findProjectLabelComponent(titleBarComponent)
    disableExternalChangesToComponentProperty(project, titleBarComponent, "background")
    disableExternalChangesToComponentProperty(project, projectLabelComponent, "foreground")

    // set the color
    gProjectColorMap[project] = color
    titleBarComponent.background = color
    recursiveSetForeground(titleBarComponent, if (ColorUtil.isDark(color)) Color.white else Color.black)

    // save color config for persistence
    PropertiesComponent.getInstance(project).setValue(COLOR_SETTING_PATH, color.rgb, 0)
}

/**
 * Set foreground recursively for given component,
 * since intellij overrides this property specifically
 * somewhere down the component tree, this needs to be set at all components
 * in order to actually change the foreground color:(
 *
 * @param container: the root component (as container)
 * @param color: color to set the foreground to
 */
fun recursiveSetForeground(container: Container, color: Color) {
    container.foreground = color
    for (comp in container.components) {
        if ((comp as Container).components.isNotEmpty()) {
            recursiveSetForeground(comp, color)
        }
        comp.foreground = color
    }
}

/**
 * Disable any unintended changes to given component of a project from external sources.
 *
 *
 * @param component: component to lock changes to.
 * @param project: the project to which the component belongs to.
 * @param property: the property which we want to lock.
 *
 * @note registers a PropertyChangeListener for given property name which `aggressively`
 * reverts the color back to the one set by this plugin.
 */
fun disableExternalChangesToComponentProperty(project: Project, component: Component, property: String) {
    if (!gColorLockedComponentMap.containsKey(component)) {
        gColorLockedComponentMap[component] = false
    }

    if (gColorLockedComponentMap[component] == false) {
        component.addPropertyChangeListener(property) {
            if (it.newValue != (gProjectColorMap[project] as Color).rgb) {
                val color = gProjectColorMap[project] as Color
                when (property) {
                    "background" -> component.background = color
                    "foreground" -> component.foreground = if (ColorUtil.isDark(color)) Color.white else Color.black
                    else -> throw InvalidParameterException("Unsupported property given!")
                }
            }
        }

        gColorLockedComponentMap[component] = true
    }
}

/**
 * Find title bar java component for given project.
 *
 * @note searches using java swing object names, which might change in future versions.
 * @throws ClassNotFoundException if none found
 * @returns the component if found, null otherwise.
 */
fun findTitleBarComponent(project: Project): Component {
    val mainIdeComponent = (WindowManager.getInstance().getFrame(project) as JFrame).getComponent(0) as Container
    var mainComponentPane: Container? = null

    for (component in mainIdeComponent.components) {
        if ("JBLayeredPane" in component.toString()) {
            mainComponentPane = component as Container
        }
    }

    if (mainComponentPane != null) {
        for (component in mainComponentPane.components) {
            if ("MenuFrameHeader" in component.toString()) {
                return component
            }
        }
    }

    throw ClassNotFoundException()
}

/**
 * Find project label component with root title bar component.
 * @throws ClassNotFoundException if none found
 */
fun findProjectLabelComponent(titleBarComponent: Container): Component {
    for (component in titleBarComponent.components) {
        if ("titleLabel" in component.toString()) {
            return (component as Container).getComponent(0)
        }
    }

    throw ClassNotFoundException()
}
