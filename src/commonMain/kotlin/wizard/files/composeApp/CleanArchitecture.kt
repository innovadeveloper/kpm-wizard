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


/**
 * crea el set de directorios siguiendo una arquitectura limpia..
 */
val cleanArchitectureForViewModelOnlyAndroid : (info : ProjectInfo) -> MutableList<ProjectFile> = {info ->
    mutableListOf<ProjectFile>(
        GenericDirectory(info, "di/module", isOnlyAndroid = false),
        GenericDirectory(info, "di/qualifiers", isOnlyAndroid = false),
        GenericDirectory(info, "data/file", isOnlyAndroid = false),
        GenericDirectory(info, "data/network", isOnlyAndroid = false),
        GenericDirectory(info, "data/repository", isOnlyAndroid = false),
        GenericDirectory(info, "data/room", isOnlyAndroid = false),
        GenericDirectory(info, "data/spf", isOnlyAndroid = false),
        GenericDirectory(info, "di", isOnlyAndroid = false),
        GenericDirectory(info, "domain/annotations", isOnlyAndroid = false),
        GenericDirectory(info, "domain/clients", isOnlyAndroid = false),
        GenericDirectory(info, "domain/constants", isOnlyAndroid = false),
        GenericDirectory(info, "domain/dto", isOnlyAndroid = false),
        GenericDirectory(info, "domain/entities", isOnlyAndroid = false),
        GenericDirectory(info, "domain/service", isOnlyAndroid = false),
        GenericDirectory(info, "domain/utils", isOnlyAndroid = false),
        GenericDirectory(info, "infrastructure/business", isOnlyAndroid = false),
        GenericDirectory(info, "infrastructure/clients", isOnlyAndroid = false),
        GenericDirectory(info, "infrastructure/extensions", isOnlyAndroid = false),
        GenericDirectory(info, "infrastructure/providers", isOnlyAndroid = false),
        GenericDirectory(info, "infrastructure/receivers", isOnlyAndroid = false),
        GenericDirectory(info, "ui/screen/counter", isOnlyAndroid = false),
        GenericDirectory(info, "ui/widget", isOnlyAndroid = false)
    )
}

class GenericDirectory(info: ProjectInfo, pathDirectory : String, isOnlyAndroid: Boolean = false) : ProjectFile {
//    override val path = "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/$pathDirectory/.gitkeep"
    override val path = if(isOnlyAndroid) "${info.moduleName}/src/main/java/${info.packagePath}/$pathDirectory/.gitkeep" else "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/$pathDirectory/.gitkeep"

    override val content = "".trimIndent()
}