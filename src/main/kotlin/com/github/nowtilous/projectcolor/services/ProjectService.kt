package com.github.nowtilous.projectcolor.services

import com.github.nowtilous.projectcolor.COLOR_SETTING_PATH
import com.github.nowtilous.projectcolor.OVERRIDE_AUTO_COLOR_CONFIG
import com.github.nowtilous.projectcolor.setTitleBarColor
import com.github.nowtilous.projectcolor.ui.AUTO_COLOR_SET_TOGGLED_PATH
import com.github.nowtilous.projectcolor.utils.getColorBasedOnProjectName
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import java.awt.Color
import java.util.Timer
import kotlin.concurrent.schedule

const val NO_COLOR_SET = -1
const val SET_COLOR_DELAY_MS: Long = 5000

class ProjectService(project: Project) {

    private val mProject = project

    init {
        val globalConfigs = PropertiesComponent.getInstance()
        val projectConfigs = PropertiesComponent.getInstance(mProject)
        val rgb = projectConfigs.getInt(COLOR_SETTING_PATH, NO_COLOR_SET)

        if (rgb != NO_COLOR_SET) {
            val color = if (globalConfigs.getBoolean(AUTO_COLOR_SET_TOGGLED_PATH) &&
                !projectConfigs.getBoolean(OVERRIDE_AUTO_COLOR_CONFIG)
            ) getColorBasedOnProjectName(mProject) else Color(rgb)

            setTitleBarColorWithDelay(color)
        } else if (globalConfigs.getBoolean(AUTO_COLOR_SET_TOGGLED_PATH)) {
            setTitleBarColorWithDelay(getColorBasedOnProjectName(mProject))
        }
    }

    /**
     * Delay this action to prevent the UI manger from overriding the title bar
     * with new components that are unreachable early in the execution
     */
    private fun setTitleBarColorWithDelay(color: Color, delay: Long = SET_COLOR_DELAY_MS) {
        Timer().schedule(delay) {
            setTitleBarColor(color, mProject)
        }
    }
}
