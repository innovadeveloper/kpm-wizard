package wizard.files.composeApp

import wizard.*


class FixResourceGeneralBuildGradle(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/fix_build.gradle.kts"

    override val content = """
plugins {
//    alias(libs.plugins.multiplatform).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
//    alias(libs.plugins.compose.multiplatform).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.ktorfit).apply(false)
    alias(libs.plugins.kotlinx.serialization).apply(false)
    alias(libs.plugins.sqlDelight).apply(false)
}
""".trimIndent()
}

class FixResourceAppBuildGradle(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/fix_app.build.gradle.kts"

    override val content = """
//import org.jetbrains.compose.ExperimentalComposeLibrary
//import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
//    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
//    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqlDelight)
}        

//https://developer.android.com/develop/ui/compose/testing#setup
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // testImplementation(libs.junit)
    // androidTestImplementation(libs.androidx.junit)
    // androidTestImplementation(libs.androidx.espresso.core)
    // androidTestImplementation(platform(libs.androidx.compose.bom))
    // androidTestImplementation(libs.androidx.ui.test.junit4)
    // debugImplementation(libs.androidx.ui.tooling)
    // debugImplementation(libs.androidx.ui.test.manifest)

//    ANDROID
//    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.cio)
    implementation(libs.sqlDelight.driver.android)
    implementation("com.abexa.kmp-libraries:shared-kmp-library:1.4.0@aar")

//    COMMON
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktorfit.lib)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.multiplatformSettings)
}
""".trimIndent()
}


class FixResourceLibToml(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/fix_libs.toml"

    override val content = """
[versions]
#agp = "8.9.2"
#kotlin = "2.0.21"
coreKtx = "1.10.1"
junit = "4.13.2"
junitVersion = "1.1.5"
espressoCore = "3.5.1"
lifecycleRuntimeKtx = "2.6.1"
activityCompose = "1.10.1"
composeBom = "2024.09.00"

kotlin = "2.1.10"
compose = "1.7.3"
agp = "8.5.2"
androidx-activityCompose = "1.10.1"
androidx-uiTest = "1.7.4"
kotlinx-coroutines = "1.10.1"
ktor = "3.1.2"
ktorfit-lib = "2.1.0"
ksp = "2.0.20-1.0.25"
ktorfit = "2.1.0"
androidx-lifecycle = "2.8.4"
kotlinx-serialization = "1.7.3"
koin = "4.0.1"
multiplatformSettings = "1.3.0"
sqlDelight = "2.0.2"
#sqlDelight = "1.5.4"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-material3 = { group = "androidx.compose.material3", name = "material3" }

#androidx-activityCompose = { module = "androidx.activity:activity-compose", version.ref = "androidx-activityCompose" }
androidx-uitest-testManifest = { module = "androidx.compose.ui:ui-test-manifest", version.ref = "androidx-uiTest" }
androidx-uitest-junit4 = { module = "androidx.compose.ui:ui-test-junit4", version.ref = "androidx-uiTest" }
kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "kotlinx-coroutines" }
kotlinx-coroutines-android = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-android", version.ref = "kotlinx-coroutines" }
kotlinx-coroutines-swing = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-swing", version.ref = "kotlinx-coroutines" }
kotlinx-coroutines-test = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-test", version.ref = "kotlinx-coroutines" }
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-client-serialization = { module = "io.ktor:ktor-client-serialization", version.ref = "ktor" }
ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
ktor-client-cio = { module = "io.ktor:ktor-client-cio", version.ref = "ktor" }
ktor-client-js = { module = "io.ktor:ktor-client-js", version.ref = "ktor" }
ktor-client-curl = { module = "io.ktor:ktor-client-curl", version.ref = "ktor" }
ktor-client-winhttp = { module = "io.ktor:ktor-client-winhttp", version.ref = "ktor" }
ktorfit-lib = { module = "de.jensklingenberg.ktorfit:ktorfit-lib", version.ref = "ktorfit-lib" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
androidx-lifecycle-viewmodel = { module = "org.jetbrains.androidx.lifecycle:lifecycle-viewmodel", version.ref = "androidx-lifecycle" }
androidx-lifecycle-runtime-compose = { module = "org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose", version.ref = "androidx-lifecycle" }
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinx-serialization" }
koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-compose = { module = "io.insert-koin:koin-compose", version.ref = "koin" }
multiplatformSettings = { module = "com.russhwolf:multiplatform-settings", version.ref = "multiplatformSettings" }
sqlDelight-driver-sqlite = { module = "app.cash.sqldelight:sqlite-driver", version.ref = "sqlDelight" }
sqlDelight-driver-android = { module = "app.cash.sqldelight:android-driver", version.ref = "sqlDelight" }
sqlDelight-driver-native = { module = "app.cash.sqldelight:native-driver", version.ref = "sqlDelight" }
sqlDelight-driver-js = { module = "app.cash.sqldelight:web-worker-driver", version.ref = "sqlDelight" }


[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }

multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
compose-compiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
compose-multiplatform = { id = "org.jetbrains.compose", version.ref = "compose" }
#android-application = { id = "com.android.application", version.ref = "agp" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
ktorfit = { id = "de.jensklingenberg.ktorfit", version.ref = "ktorfit" }
kotlinx-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
sqlDelight = { id = "app.cash.sqldelight", version.ref = "sqlDelight" }
#sqlDelight = { id = "com.squareup.sqldelight", version.ref = "sqlDelight" }
""".trimIndent()
}




class CommonAppKoin(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/App.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/App.kt"

    override val content = """
package ${info.packageId}

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import ${if(isOnlyAndroid) "androidx.compose.ui.tooling.preview.Preview" else "org.jetbrains.compose.ui.tooling.preview.Preview"} 
import ${info.packageId}.ui.screen.counter.CounterScreenClass
import ${info.packageId}.ui.screen.counter.CounterViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        CounterScreenClass().build<CounterViewModel>()
    }
}
""".trimIndent()
}

class AppKt(info: ProjectInfo, isOnlyAndroid : Boolean = false) : ProjectFile {
//    override val path = "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/App.kt"
    override val path = if(isOnlyAndroid) "${info.moduleName}/src/main/java/${info.packagePath}/App.kt" else "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/App.kt"

    override val content = """
        package ${info.packageId}
        
        import androidx.compose.animation.core.*
        import androidx.compose.foundation.Image
        import androidx.compose.foundation.layout.*
        import androidx.compose.material3.*
        import androidx.compose.runtime.*
        import androidx.compose.ui.Alignment
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.draw.rotate
        import androidx.compose.ui.graphics.ColorFilter
        import androidx.compose.ui.platform.LocalUriHandler
        import androidx.compose.ui.text.font.FontFamily
        import androidx.compose.ui.unit.dp
        import ${info.getResourcesPackage()}.*
        import ${info.packageId}.theme.AppTheme
        import ${info.packageId}.theme.LocalThemeIsDark
        import kotlinx.coroutines.isActive
        import org.jetbrains.compose.resources.Font
        import org.jetbrains.compose.resources.stringResource
        import org.jetbrains.compose.resources.vectorResource

        @Composable
        internal fun App() = AppTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.cyclone),
                    fontFamily = FontFamily(Font(Res.font.IndieFlower_Regular)),
                    style = MaterialTheme.typography.displayLarge
                )

                var isRotating by remember { mutableStateOf(false) }

                val rotate = remember { Animatable(0f) }
                val target = 360f
                if (isRotating) {
                    LaunchedEffect(Unit) {
                        while (isActive) {
                            val remaining = (target - rotate.value) / target
                            rotate.animateTo(target, animationSpec = tween((1_000 * remaining).toInt(), easing = LinearEasing))
                            rotate.snapTo(0f)
                        }
                    }
                }

                Image(
                    modifier = Modifier
                        .size(250.dp)
                        .padding(16.dp)
                        .run { rotate(rotate.value) },
                    imageVector = vectorResource(Res.drawable.ic_cyclone),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    contentDescription = null
                )

                ElevatedButton(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .widthIn(min = 200.dp),
                    onClick = { isRotating = !isRotating },
                    content = {
                        Icon(vectorResource(Res.drawable.ic_rotate_right), contentDescription = null)
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            stringResource(if (isRotating) Res.string.stop else Res.string.run)
                        )
                    }
                )

                var isDark by LocalThemeIsDark.current
                val icon = remember(isDark) {
                    if (isDark) Res.drawable.ic_light_mode
                    else Res.drawable.ic_dark_mode
                }

                ElevatedButton(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
                    onClick = { isDark = !isDark },
                    content = {
                        Icon(vectorResource(icon), contentDescription = null)
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(stringResource(Res.string.theme))
                    }
                )

                val uriHandler = LocalUriHandler.current
                TextButton(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp).widthIn(min = 200.dp),
                    onClick = { uriHandler.openUri("https://github.com/terrakok") },
                ) {
                    Text(stringResource(Res.string.open_github))
                }
            }
        }
        
    """.trimIndent()
}

class AndroidAppKt(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/App.android.kt"
    else
        "${info.moduleName}/src/androidMain/kotlin/${info.packagePath}/App.android.kt"

    override val content = if(isOnlyAndroid)"""
        package ${info.packageId}

        import android.os.Bundle
        import androidx.activity.ComponentActivity
        import androidx.activity.compose.setContent
        import ${info.packageId}.ui.screen.counter.CounterScreenClass
        import ${info.packageId}.ui.screen.counter.CounterViewModel

        class MainActivity : ComponentActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContent {
                    CounterScreenClass().build<CounterViewModel>()
                }
            }
        }
    """.trimIndent() else
        """
        package ${info.packageId}

        import android.os.Bundle
        import androidx.activity.ComponentActivity
        import androidx.activity.compose.setContent
        import androidx.activity.enableEdgeToEdge
        import androidx.compose.runtime.Composable
        import androidx.compose.ui.tooling.preview.Preview
        
        class AppActivity : ComponentActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                enableEdgeToEdge()
                setContent { App() }
            }
        }
        
        @Preview
        @Composable
        fun AppPreview() { App() }
        
    """.trimIndent()
}


class AndroidAppApplicationKt(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/AppApplication.android.kt"
    else
        "${info.moduleName}/src/androidMain/kotlin/${info.packagePath}/AppApplication.android.kt"

    override val content = """
        package ${info.packageId}
        
        import android.app.Application
        import android.content.Context
        import ${info.packageId}.di.androidModule
        import ${info.packageId}.di.initKoin
        
        class AppApplication : Application(){
        
            companion object{
                lateinit var appContext : Context
            }
        
            override fun onCreate() {
                super.onCreate()
                AppApplication.appContext = this
                initKoin(androidModule(AppApplication.appContext))
            }
        }
    """.trimIndent()
}


class AndroidDatabaseDriverFactoryKt(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/data/db/DatabaseDriverFactory.android.kt"
    else
        "${info.moduleName}/src/androidMain/kotlin/${info.packagePath}/data/db/DatabaseDriverFactory.android.kt"

    override val content = """
        package ${info.packageId}.data.db
        
        import android.content.Context
        import app.cash.sqldelight.db.SqlDriver
        import app.cash.sqldelight.driver.android.AndroidSqliteDriver
        import ${info.packageId}.db.MyDatabase
        ${if(isOnlyAndroid) """
class DatabaseDriverFactory(private val context: Context) : DBDriver {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(MyDatabase.Schema, context, "mydatabase.db")
    }
}""" else """
actual class DatabaseDriverFactory(private val context: Context) : DBDriver {
    actual override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(MyDatabase.Schema, context, "mydatabase.db")
    }
}"""}""".trimIndent()
}

class AndroidPreferencesConfigKt(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/data/spf/PreferencesConfig.android.kt"
    else
        "${info.moduleName}/src/androidMain/kotlin/${info.packagePath}/data/spf/PreferencesConfig.android.kt"

    override val content = """
        package ${info.packageId}.data.spf
        
        import android.content.Context
        import ${info.packageId}.AppApplication
        import ${info.packageId}.domain.constants.Constants
        import com.russhwolf.settings.Settings
        import com.russhwolf.settings.SharedPreferencesSettings
        
        actual fun provideSettings(): Settings {
            val sharedPreferences = AppApplication.appContext.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE)
            return SharedPreferencesSettings(sharedPreferences)
        }
    """.trimIndent()
}

class AndroidKoinModuleKt(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/di/AndroidKoinModule.kt"
    else
        "${info.moduleName}/src/androidMain/kotlin/${info.packagePath}/di/AndroidKoinModule.kt"

    override val content = """
        package ${info.packageId}.di
        
        import android.content.Context
        import ${info.packageId}.data.db.DBDriver
        import ${info.packageId}.data.db.DatabaseDriverFactory
        import org.koin.dsl.module
        
        fun androidModule(context: Context) = module {
            single { "Soy un servicio Android específico" }
            single<DBDriver> { DatabaseDriverFactory(context) }
        }
    """.trimIndent()
}
