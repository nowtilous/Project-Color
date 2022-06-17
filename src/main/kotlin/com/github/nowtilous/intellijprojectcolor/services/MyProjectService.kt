package com.github.nowtilous.intellijprojectcolor.services

import com.intellij.openapi.project.Project
import com.github.nowtilous.intellijprojectcolor.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
