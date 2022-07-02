package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.utils.OSTypeFactory
import com.intellij.openapi.externalSystem.service.execution.NotSupportedException

/**
 * Color setter factory based on current OS type.
 *
 * This static class acts both as a singleton for mColorSetter and
 * as a factory for the ColorSetter classes.
 */
object ColorSetterFactory {
    private val mColorSetterMap = mapOf(
        (OSTypeFactory.OSType.Windows to WindowsColorSetter()),
        (OSTypeFactory.OSType.MacOS to MacOSColorSetter())
    )

    private lateinit var mColorSetter: ColorSetter

    fun getColorSetter(): ColorSetter {
        if (!::mColorSetter.isInitialized) {
            val osType = OSTypeFactory.getOperatingSystemType()
            if (osType in mColorSetterMap) {
                mColorSetter = mColorSetterMap[osType]!!
            } else {
                throw NotSupportedException("Current OS type is not supported!")
            }
        }
        return mColorSetter
    }
}

