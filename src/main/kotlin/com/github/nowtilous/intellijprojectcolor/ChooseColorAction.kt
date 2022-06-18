package com.github.nowtilous.intellijprojectcolor

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import java.awt.Color
import java.awt.Component
import java.awt.Container
import javax.swing.JColorChooser
import javax.swing.JFrame

const val COLOR_SETTING_PATH = "com.github.nowtilous.intellijprojectcolor.rgb"

val componentTrackedMap = mutableMapOf<Component, Boolean>()
val currentProjectColor = mutableMapOf<Project, Color>()

class ChooseColorAction : AnAction() {

    private val mChooser = JColorChooser()
    private lateinit var mActionEvent: AnActionEvent
    override fun actionPerformed(e: AnActionEvent) {
        mActionEvent = e
        mChooser.selectionModel.addChangeListener {
            setTitleBarColor(mChooser.color, mActionEvent.dataContext.getData(PlatformDataKeys.PROJECT) as Project)
        }
        val dialog = JColorChooser.createDialog(null, "Choose a title bar color", true, mChooser, null, null)
        dialog.isVisible = true
    }

}
fun setTitleBarColor(color: Color, project: Project) {
    val mainIdeComponent = (WindowManager.getInstance().getFrame(project) as JFrame).getComponent(0) as Container
    var mainComponentPane: Container? = null
    var titleBarComponent: Component? = null

    for (component in mainIdeComponent.components){
        if("JBLayeredPane" in component.toString()){
            mainComponentPane = component as Container
        }
    }
    if (mainComponentPane == null) {
        return
    }

    for(component in mainComponentPane.components){
        if ("MenuFrameHeader" in component.toString()){
            titleBarComponent = component
        }
    }

    if (titleBarComponent == null) {
        return
    }


    if(!componentTrackedMap.containsKey(titleBarComponent)){
        componentTrackedMap[titleBarComponent] = false
    }

    if(componentTrackedMap[titleBarComponent] == false){
        titleBarComponent.addPropertyChangeListener("background") {
            if (it.newValue != (currentProjectColor[project] as Color).rgb) {
                titleBarComponent.background = currentProjectColor[project]
            }
        }

        componentTrackedMap[titleBarComponent] = true
    }

    currentProjectColor[project] = color
    titleBarComponent.background = color

    PropertiesComponent.getInstance(project).setValue(COLOR_SETTING_PATH,color.rgb, 0)
}

