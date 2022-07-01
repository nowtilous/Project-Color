package com.github.nowtilous.projectcolor.color_setters

import com.github.nowtilous.projectcolor.utils.OSTypeFactory

val gColorSetterMap = mapOf<OSTypeFactory.OSType, ColorSetter>((OSTypeFactory.OSType.Windows to WindowsColorSetter()))

lateinit var gColorSetter: ColorSetter