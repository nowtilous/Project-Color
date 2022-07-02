package com.github.nowtilous.projectcolor.services

import com.github.nowtilous.projectcolor.COLOR_SETTING_PATH
import com.github.nowtilous.projectcolor.setTitleBarColor
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import java.awt.Color
import java.util.Timer
import kotlin.concurrent.schedule

const val NO_COLOR_SET = -1
const val SET_COLOR_DELAY_MS: Long = 5000

class ProjectService(project: Project) {
    init {
        val rgb = PropertiesComponent.getInstance(project).getInt(COLOR_SETTING_PATH, NO_COLOR_SET)
        if (rgb != NO_COLOR_SET) {
            // delay this action to prevent the UI manger from overriding the title bar
            // with new components that are unreachable this early in the execution
            Timer().schedule(SET_COLOR_DELAY_MS) {
                setTitleBarColor(Color(rgb), project)
            }
        }
    }
}
