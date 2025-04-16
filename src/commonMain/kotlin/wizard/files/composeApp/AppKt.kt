package wizard.files.composeApp

import wizard.*

class AppKt(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/App.kt"
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

class AndroidAppKt(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/src/androidMain/kotlin/${info.packagePath}/App.android.kt"
    override val content = """
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


class AndroidAppApplicationKt(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/src/androidMain/kotlin/${info.packagePath}/AppApplication.android.kt"
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
