package wizard.files

import wizard.ProjectFile
import wizard.ProjectInfo
import wizard.ProjectPlatform
import wizard.WizardType
import wizard.needComposeSample
import wizard.needTerminalSample
import wizard.safeName

class SettingsGradleKts(info: ProjectInfo) : ProjectFile {
    override val path = "settings.gradle.kts"
    override val content = buildString {
        appendLine("rootProject.name = \"${info.safeName}\"")
        appendLine("")
        append("""
            |pluginManagement {
            |    repositories {
            |        google {
            |            mavenContent {
            |                includeGroupAndSubgroups("androidx")
            |                includeGroupAndSubgroups("com.android")
            |                includeGroupAndSubgroups("com.google")
            |            }
            |        }
            |        mavenCentral()
            |        gradlePluginPortal()
            |    }
            |}
            |
            |dependencyResolutionManagement {
            |    repositories {
            |        google {
            |            mavenContent {
            |                includeGroupAndSubgroups("androidx")
            |                includeGroupAndSubgroups("com.android")
            |                includeGroupAndSubgroups("com.google")
            |            }
            |        }
            |        mavenCentral()
            |        maven {
            |            url = uri("https://artifactory-service.abexa.pe/repository/android-libraries-releases/")
            |            credentials {
            |                username = "xxxxx"
            |                password = "xxxxx"
            |            }
            |        }
            |    }
            |}
        """.trimMargin())
        appendLine("")
        if (info.type == WizardType.KmpLibrary) appendLine("includeBuild(\"convention-plugins\")")
        appendLine("include(\":${info.moduleName}\")")
        if (info.needComposeSample) appendLine("include(\":sample:composeApp\")")
        if (info.needTerminalSample) appendLine("include(\":sample:terminalApp\")")
        appendLine("")
    }
}