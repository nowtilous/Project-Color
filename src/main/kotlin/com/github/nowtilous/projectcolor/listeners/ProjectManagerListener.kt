package com.github.nowtilous.projectcolor.listeners

import com.github.nowtilous.projectcolor.color_setters.gColorSetter
import com.github.nowtilous.projectcolor.color_setters.gColorSetterMap
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.github.nowtilous.projectcolor.services.ProjectService
import com.github.nowtilous.projectcolor.utils.OSTypeFactory
import com.intellij.openapi.externalSystem.service.execution.NotSupportedException


internal class ProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {

        val osType = OSTypeFactory.getOperatingSystemType()
        if (osType in gColorSetterMap) {
            gColorSetter = gColorSetterMap[osType]!!
            project.service<ProjectService>()
        } else {
            throw NotSupportedException("Current OS type is not supported!")
        }
    }
}
