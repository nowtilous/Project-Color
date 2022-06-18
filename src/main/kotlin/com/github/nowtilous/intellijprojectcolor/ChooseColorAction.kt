package com.github.nowtilous.intellijprojectcolor

import com.esotericsoftware.kryo.NotNull
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataConstants
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.WindowManager
import java.awt.Color
import java.awt.Component
import java.awt.Container
import javax.swing.JColorChooser
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.event.ChangeEvent
import javax.swing.plaf.ColorUIResource

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
                println("CHANGED")
                titleBarComponent.background = currentProjectColor[project]
            }
        }

        componentTrackedMap[titleBarComponent] = true
    }

    currentProjectColor[project] = color
    titleBarComponent.background = color
//    PropertiesComponent.getInstance(project).setValue("r", newColor.red, newColor.red)
//    PropertiesComponent.getInstance(project).setValue("g", newColor.green, newColor.green)
//    PropertiesComponent.getInstance(project).setValue("b", newColor.blue, newColor.blue)
    println(color)
}

