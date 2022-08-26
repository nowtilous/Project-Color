package com.github.nowtilous.projectcolor.listeners

import com.github.nowtilous.projectcolor.color_setters.ColorSetterFactory
import com.github.nowtilous.projectcolor.gColorLockedComponentMap
import com.github.nowtilous.projectcolor.gProjectColorLockedMap
import com.github.nowtilous.projectcolor.gProjectColorMap
import com.github.nowtilous.projectcolor.gProjectLockedComponentsMap
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.github.nowtilous.projectcolor.services.ProjectService

internal class ProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        project.service<ProjectService>()
    }

    override fun projectClosed(project: Project) {
        val currentColorSetter = ColorSetterFactory.getColorSetter()
        try {
            gProjectColorMap.remove(project)
            gProjectColorLockedMap.remove(project)
            for(component in gProjectLockedComponentsMap[project]!!){
                gColorLockedComponentMap.remove(component)
            }
            gProjectLockedComponentsMap.remove(project)
            currentColorSetter.cleanUp(project)
        } catch (_: Exception) {
        }
    }
}
