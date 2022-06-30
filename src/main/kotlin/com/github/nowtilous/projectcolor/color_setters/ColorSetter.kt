package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.gColorLockedComponentMap
import com.github.nowtilous.projectcolor.gProjectColorMap
import com.intellij.openapi.project.Project
import com.intellij.ui.ColorUtil
import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.security.InvalidParameterException

abstract class ColorSetter {

    abstract fun setTitleBar(color: Color, project: Project)

    abstract fun findTitleBarComponent(project: Project): Component

    /**
     * Set foreground recursively for given component,
     * since intellij overrides this property specifically
     * somewhere down the component tree, this needs to be set at all components
     * in order to actually change the foreground color:(
     *
     * @param container: the root component (as container)
     * @param color: color to set the foreground to
     */
    protected fun recursiveSetForeground(container: Container, color: Color) {
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

}