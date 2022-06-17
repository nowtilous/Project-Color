package com.github.nowtilous.intellijprojectcolor.services

import com.intellij.openapi.project.Project
import com.github.nowtilous.intellijprojectcolor.MyBundle
import com.intellij.openapi.wm.WindowManager
import javax.swing.UIManager

class MyProjectService(project: Project) {

    init {
        UIManager.put("TitlePane.background",javax.swing.plaf.ColorUIResource(250,250,250))
        println(MyBundle.message("projectService", project.name))
    }
}
