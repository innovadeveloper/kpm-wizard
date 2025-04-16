package wizard.files.composeApp

import wizard.ProjectFile
import wizard.ProjectInfo
import wizard.packagePath

/**
 * crea el set de directorios siguiendo una arquitectura limpia..
 */
val cleanArchitectureForViewModel : (info : ProjectInfo) -> MutableList<ProjectFile> = {info ->
    mutableListOf<ProjectFile>(
        GenericDirectory(info, "di/module"),
        GenericDirectory(info, "di/qualifiers"),
        GenericDirectory(info, "data/file"),
        GenericDirectory(info, "data/network"),
        GenericDirectory(info, "data/repository"),
        GenericDirectory(info, "data/room"),
        GenericDirectory(info, "data/spf"),
        GenericDirectory(info, "di"),
        GenericDirectory(info, "domain/annotations"),
        GenericDirectory(info, "domain/clients"),
        GenericDirectory(info, "domain/constants"),
        GenericDirectory(info, "domain/dto"),
        GenericDirectory(info, "domain/entities"),
        GenericDirectory(info, "domain/service"),
        GenericDirectory(info, "domain/utils"),
        GenericDirectory(info, "infrastructure/business"),
        GenericDirectory(info, "infrastructure/clients"),
        GenericDirectory(info, "infrastructure/extensions"),
        GenericDirectory(info, "infrastructure/providers"),
        GenericDirectory(info, "infrastructure/receivers"),
        GenericDirectory(info, "ui/screen/counter"),
        GenericDirectory(info, "ui/widget")
    )
}
class GenericDirectory(info: ProjectInfo, pathDirectory : String) : ProjectFile {
    override val path = "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/$pathDirectory/.gitkeep"
    override val content = "".trimIndent()
}