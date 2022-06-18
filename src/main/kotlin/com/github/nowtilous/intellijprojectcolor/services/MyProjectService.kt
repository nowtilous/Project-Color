package com.github.nowtilous.intellijprojectcolor.services

import com.github.nowtilous.intellijprojectcolor.ChooseColorAction
import com.github.nowtilous.intellijprojectcolor.MyBundle
import com.github.nowtilous.intellijprojectcolor.setTitleBarColor
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.project.Project
import java.awt.Color
import javax.swing.UIManager

//class MyProjectService(project: Project) : PersistentStateComponent<MyProjectService.State> {
class MyProjectService(project: Project) {

//    private var myState = State()
    init {
//        if (myState.r != null && myState.g != null && myState.b!= null){
//            setTitleBarColor(Color(myState.r!!, myState.g!!, myState.b!!), project)
//        }
        println(MyBundle.message("projectService", project.name))
    }

//    class State {
//        var r: Int? = null
//        var g: Int? = null
//        var b: Int? = null
//    }
//    override fun getState(): State {
//        return myState
//    }
//
//    override fun loadState(state: State) {
//        myState = state;
//    }
}
