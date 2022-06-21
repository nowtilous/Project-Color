package com.github.nowtilous.projectcolor.services

import com.github.nowtilous.projectcolor.COLOR_SETTING_PATH
import com.github.nowtilous.projectcolor.setTitleBarColor
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import java.awt.Color

const val NO_COLOR_SET = -1
class ProjectService(project: Project) {
    init {
        val color = PropertiesComponent.getInstance(project).getInt(COLOR_SETTING_PATH, NO_COLOR_SET)
        if (color != NO_COLOR_SET){
            setTitleBarColor(Color(color), project)
        }
    }
}
