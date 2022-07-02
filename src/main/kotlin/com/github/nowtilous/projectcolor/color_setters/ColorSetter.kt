package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.gColorLockedComponentMap
import com.github.nowtilous.projectcolor.gProjectColorMap
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.ColorUtil
import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.security.InvalidParameterException
import javax.swing.JFrame

abstract class ColorSetter {

    // Title bar's component path down from the main ide component (differs between operating systems)
    abstract val TITLE_BAR_COMPONENT_PATH: List<String>

    abstract fun setTitleBar(color: Color, project: Project)

    /**
     * Set a given property's color recursively for given component.
     *
     * @param container: the root component (as container)
     * @param color: color to set the property to
     * @param property: property to repaint
     */
    protected fun recursiveSetComponentColor(container: Container, color: Color, property: String) {

        when (property) {
            "background" -> container.background = color
            "foreground" -> container.foreground = color
            else -> throw InvalidParameterException("Unsupported property given!")
        }

        for (comp in container.components) {
            if ((comp as Container).components.isNotEmpty()) {
                recursiveSetComponentColor(comp, color, property)
            }
        }
    }

    /**
     * Disable any unintended changes to given component of a project from external sources.
     *
     * @param component: component to lock changes to.
     * @param project: the project to which the component belongs to.
     * @param property: the property which we want to lock.
     *
     * @note registers a PropertyChangeListener for given property name which `aggressively`
     * reverts the color back to the one set by this plugin.
     */
    protected fun lockComponentColorProperty(project: Project, component: Component, property: String) {
        if (!gColorLockedComponentMap.containsKey(component)) {
            gColorLockedComponentMap[component] = false
        }

        if (gColorLockedComponentMap[component] == false) {
            component.addPropertyChangeListener(property) {
                if (project in gProjectColorMap && it.newValue != (gProjectColorMap[project] as Color).rgb) {
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

    protected open fun findTitleBarComponent(project: Project): Container {
        val mainIdeComponent = (WindowManager.getInstance().getFrame(project) as JFrame).getComponent(0) as Container

        return findComponent(mainIdeComponent, TITLE_BAR_COMPONENT_PATH) as Container
    }

    protected fun findComponent(root: Container, path: List<String>): Component {
        var currentComponent = root
        var found = false

        for (componentName in path) {
            for (component in currentComponent.components) {
                if (componentName in component.toString()) {
                    currentComponent = component as Container
                    found = true
                    break
                }
            }

            if (!found) throw ClassNotFoundException()
            found = false
        }

        return currentComponent
    }
}