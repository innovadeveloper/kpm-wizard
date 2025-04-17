package wizard

import wizard.dependencies.AndroidxLifecycleViewmodel
import wizard.dependencies.ApolloPlugin
import wizard.dependencies.Koin
import wizard.dependencies.SQLDelightPlugin
import wizard.files.*
import wizard.files.composeApp.*

fun ProjectInfo.generateComposeAppFiles(): List<ProjectFile> = buildList {
    val info = this@generateComposeAppFiles

    add(Gitignore())
    add(Readme(info))

    add(GradleBat())
    add(Gradlew())
    add(GradleWrapperProperties(info))
    add(GradleWrapperJar())
    add(GradleLibsVersion(info))

    add(GradleProperties(info))
    add(RootBuildGradleKts(info))
    add(SettingsGradleKts(info))

    add(ModuleBuildGradleKts(info))
    add(ColorKt(info))
    add(ThemeKt(info))
    add(AppKt(info))
    add(ComposeTestKt(info))

    add(IcCycloneXml(info))
    add(IcDarkModeXml(info))
    add(IcLightModeXml(info))
    add(IcRotateRightXml(info))
    add(StringsXml(info))
    add(IndieFlowerTtf(info))

    // para todas las plataformas se creará la siguiente estructura sin excepción
    addAll(cleanArchitectureForViewModel(info))
    if (info.dependencies.contains(SQLDelightPlugin) && info.dependencies.contains(Koin)) {
        add(CommonAbstractScreen(info))
        add(CommonAbstractViewModel(info))
        add(CommonCounterScreen(info))
        add(CommonCounterViewModel(info))

        // sqldelight
        add(CommonDatabaseDriverFactoryExpect(info))
        add(CommonDBDriver(info))
        add(CommonSchemasSQLDelight(info))
        add(CommonUsersSQLDelight(info))
        add(CommonOrdersSQLDelight(info))
        add(CommonDBRepository(info))

        // koin
//        com.abexa.kmp.di.qualifiers
        add(CommonServiceKoinQualifier(info))
        add(CommonNetworkKoinQualifier(info))
        add(CommonKoinInit(info))
        add(CommonKoinService(info))
        add(CommonHTTPGenericProvider(info))
        add(CommonPreferencesSPF(info))
        add(CommonPreferencesConfig(info))
        add(CommonNetworkIdentityServerAPI(info))
        add(CommonNetworkAnotherServerAPI(info))
        add(CommonConstants(info))
        add(CommonAccessTokenDTO(info))
        add(CommonRequestTokenDTO(info))
        add(CommonAppParamsDTO(info))
        add(CommonExtensions(info))

        add(CommonExtensions(info))
        add(CommonExtensions(info))
        add(CommonAppKoin(info))


    }


    if (info.dependencies.contains(ApolloPlugin)) {
        add(GraphQLSchema(info))
        add(GraphQLQuery(info))
    }

    if (info.hasPlatform(ProjectPlatform.Android)) {
        add(SimpleAndroidManifest(info))
        addAll(AndroidAppIcons(info))
        add(AndroidAppKt(info))
        add(AndroidThemeKt(info))

        if (info.dependencies.contains(SQLDelightPlugin) && info.dependencies.contains(
                AndroidxLifecycleViewmodel
            ) && info.dependencies.contains(Koin)
        ) {
            add(AndroidManifest(info))
            add(AndroidAppApplicationKt(info))
            add(AndroidKoinModuleKt(info))
            add(AndroidDatabaseDriverFactoryKt(info))
            add(AndroidPreferencesConfigKt(info))
        }
    }

    if (info.hasPlatform(ProjectPlatform.Jvm)) {
        add(DesktopMainKt(info))
        add(DesktopThemeKt(info))
        addAll(DesktopAppIcons(info))

        if (info.dependencies.contains(SQLDelightPlugin) && info.dependencies.contains(Koin)) {
            add(DesktopDatabaseDriverFactoryKt(info))
            add(DesktopFileSettingsProviderKt(info))
            add(DesktopPreferencesConfigKt(info))
            add(DesktopKoinModuleKt(info))
        }
    }

    if (info.hasPlatform(ProjectPlatform.Ios)) {
        add(IosMainKt(info))
        add(IosThemeKt(info))

        addAll(IosAppIcons())
        add(IosAccentColor())
        add(IosAssets())
        add(IosPreviewAssets())
        add(IosAppSwift())
        add(IosXcworkspace())
        add(IosPbxproj(info))
        add(IosInfoPlist())
    }

    if (info.hasPlatform(ProjectPlatform.Js)) {
        add(JsIndexHtml(info))
        add(JsMainKt(info))
        add(JsThemeKt(info))
        add(JsFavicon(info))
    }

    if (info.hasPlatform(ProjectPlatform.Wasm)) {
        add(WasmJsIndexHtml(info))
        add(WasmJsMainKt(info))
        add(WasmJsThemeKt(info))
        add(WasmJsFavicon(info))
    }
}