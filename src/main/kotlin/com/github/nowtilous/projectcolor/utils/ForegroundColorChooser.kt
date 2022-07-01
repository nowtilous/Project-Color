package com.github.nowtilous.projectcolor.utils

import com.intellij.ui.ColorUtil
import java.awt.Color

/**
 * Get appropriate foreground text color based on brightness of given color.
 *
 * @returns white if color is dark, and black if color is bright
 */
fun getForegroundColorBasedOnBrightness(color: Color): Color {
    return if (ColorUtil.isDark(color)) Color.white else Color.black
}