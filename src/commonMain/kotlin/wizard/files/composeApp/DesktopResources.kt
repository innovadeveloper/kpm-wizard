package wizard.files.composeApp

import wizard.ProjectFile
import wizard.ProjectInfo
import wizard.packagePath


class DesktopDatabaseDriverFactoryKt(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/src/desktopMain/kotlin/${info.packagePath}/data/db/DatabaseDriverFactory.desktop.kt"
    override val content = """
        package ${info.packageId}.data.db
        
        import app.cash.sqldelight.db.SqlDriver
        import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
        import ${info.packageId}.db.MyDatabase
        
        actual class DatabaseDriverFactory : DBDriver {
            actual override fun createDriver(): SqlDriver {
                return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
                    MyDatabase.Schema.create(this)
                }
            }
        }
    """.trimIndent()
}

class DesktopFileSettingsProviderKt(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/src/desktopMain/kotlin/${info.packagePath}/data/spf/FileSettingsProvider.kt"
    override val content = """
        package ${info.packageId}.data.spf
        
        import com.russhwolf.settings.Settings
        import java.io.File
        
        class FileSettingsProvider(private val file: File) : Settings {
        
            private val data: MutableMap<String, String> = mutableMapOf()
        
            init {
                if (file.exists()) {
                    file.readLines().forEach { line ->
                        val (key, value) = line.split("=", limit = 2)
                        data[key] = value
                    }
                }
            }
        
            override val size: Int
                get() = data.keys.size
        
            override val keys: Set<String>
                get() = data.keys
        
            override fun hasKey(key: String): Boolean = data.containsKey(key)
        
            override fun putString(key: String, value: String) {
                data[key] = value
                saveToFile()
            }
        
            override fun getString(key: String, defaultValue: String): String {
                return data[key] ?: defaultValue
            }
        
            override fun getStringOrNull(key: String): String? {
                return data[key]
            }
        
            override fun putInt(key: String, value: Int) {
                putString(key, value.toString())
            }
        
            override fun getInt(key: String, defaultValue: Int): Int {
                return data[key]?.toIntOrNull() ?: defaultValue
            }
        
            override fun getIntOrNull(key: String): Int? {
                return data[key]?.toIntOrNull()
            }
        
            override fun putBoolean(key: String, value: Boolean) {
                putString(key, value.toString())
            }
        
            override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
                return data[key]?.toBoolean() ?: defaultValue
            }
        
            override fun getBooleanOrNull(key: String): Boolean? {
                return data[key]?.toBooleanStrictOrNull()
            }
        
            override fun putDouble(key: String, value: Double) {
                putString(key, value.toString())
            }
        
            override fun getDouble(key: String, defaultValue: Double): Double {
                return data[key]?.toDoubleOrNull() ?: defaultValue
            }
        
            override fun getDoubleOrNull(key: String): Double? {
                return data[key]?.toDoubleOrNull()
            }
        
            override fun putFloat(key: String, value: Float) {
                putString(key, value.toString())
            }
        
            override fun getFloat(key: String, defaultValue: Float): Float {
                return data[key]?.toFloatOrNull() ?: defaultValue
            }
        
            override fun getFloatOrNull(key: String): Float? {
                return data[key]?.toFloatOrNull()
            }
        
            override fun putLong(key: String, value: Long) {
                putString(key, value.toString())
            }
        
            override fun getLong(key: String, defaultValue: Long): Long {
                return data[key]?.toLongOrNull() ?: defaultValue
            }
        
            override fun getLongOrNull(key: String): Long? {
                return data[key]?.toLongOrNull()
            }
        
            override fun remove(key: String) {
                data.remove(key)
                saveToFile()
            }
        
            override fun clear() {
                data.clear()
                saveToFile()
            }
        
            private fun saveToFile() {
                file.writeText(data.entries.joinToString("\n") { "${'$'}{it.key}=${'$'}{it.value}" })
            }
        }
    """.trimIndent()
}

class DesktopPreferencesConfigKt(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/src/desktopMain/kotlin/${info.packagePath}/data/spf/PreferencesConfig.desktop.kt"
    override val content = """
        package ${info.packageId}.data.spf

        import ${info.packageId}.domain.constants.Constants
        import com.russhwolf.settings.Settings
        import java.io.File
        
        actual fun provideSettings(): Settings {
            val file = File(Constants.PREFERENCES_NAME)
            return FileSettingsProvider(file)
        }
    """.trimIndent()
}

class DesktopKoinModuleKt(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/src/desktopMain/kotlin/${info.packagePath}/data/di/DesktopKoinModule.kt"
    override val content = """
        package ${info.packageId}.data.di
        
        import ${info.packageId}.data.db.DBDriver
        import ${info.packageId}.data.db.DatabaseDriverFactory
        import org.koin.dsl.module
        
        fun desktopModule() = module {
            single { "Soy un servicio Desktop específico" }
            single<DBDriver> { DatabaseDriverFactory() }
        }
    """.trimIndent()
}


