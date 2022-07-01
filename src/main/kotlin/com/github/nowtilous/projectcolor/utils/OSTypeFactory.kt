package com.github.nowtilous.projectcolor.utils

/**
 * helper class to check the operating system this Java VM runs in
 */

class OSTypeFactory {
    /**
     * types of Operating Systems
     */
    enum class OSType {
        Windows, MacOS, Linux, Other
    }

    companion object {
        /**
         * detect the operating system from the os.name System property and cache
         * the result.
         */
        fun getOperatingSystemType(): OSType {
            val os: String = System.getProperty("os.name").toLowerCase()

            val detectedOS = if (os.contains("mac")) {
                OSType.MacOS
            } else if (os.contains("win")) {
                OSType.Windows
            } else if (os.contains("linux")) {
                OSType.Linux
            } else {
                OSType.Other
            }

            return detectedOS
        }
    }
}