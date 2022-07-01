package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.utils.OSTypeFactory
import com.intellij.openapi.externalSystem.service.execution.NotSupportedException

private val gColorSetterMap = mapOf(
    (OSTypeFactory.OSType.Windows to WindowsColorSetter()),
    (OSTypeFactory.OSType.MacOS to MacOSColorSetter())
)

private lateinit var gColorSetter: ColorSetter
fun getColorSetter(): ColorSetter {
    if (!::gColorSetter.isInitialized) {
        val osType = OSTypeFactory.getOperatingSystemType()
        if (osType in gColorSetterMap) {
            gColorSetter = gColorSetterMap[osType]!!
        } else {
            throw NotSupportedException("Current OS type is not supported!")
        }
    }
    return gColorSetter
}