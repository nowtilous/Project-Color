package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.utils.OSTypeFactory

val gColorSetterMap = mapOf(
    (OSTypeFactory.OSType.Windows to WindowsColorSetter()),
    (OSTypeFactory.OSType.MacOS to MacOSColorSetter())
)

lateinit var gColorSetter: ColorSetter