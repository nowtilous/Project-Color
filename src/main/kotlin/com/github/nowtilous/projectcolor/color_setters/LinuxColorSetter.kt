package com.github.nowtilous.projectcolor.color_setters

/**
 * Linux's layout is very similar to MacOs's, only difference
 * being the component tree path to the menu bar.
 */
class LinuxColorSetter : MacOSColorSetter() {
    override val TITLE_BAR_COMPONENT_PATH = listOf("JBLayeredPane", "JBPanel", "JBBox")
}