package wizard.files.composeApp

import wizard.ProjectFile
import wizard.ProjectInfo
import wizard.packagePath

// requiere koin, viewmodel
class CommonAbstractScreen(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/ui/screen/AbstractScreen.kt"
    override val content = """
        package ${info.packageId}.ui.screen
        
        import androidx.compose.runtime.Composable
        import androidx.compose.runtime.DisposableEffect
        import androidx.compose.runtime.LaunchedEffect
        import androidx.compose.runtime.collectAsState
        import androidx.compose.runtime.getValue
        import androidx.compose.runtime.remember
        import org.koin.core.context.GlobalContext
        import org.koin.core.qualifier.named
        
        abstract class AbstractScreen<T : AbstractViewModel<State>, State : Any>() {
        
            @Composable
            inline fun <reified VM : T> build() {
                val viewModel : T = remember { GlobalContext.get().get(VM::class, qualifier = named(namedQualifier())) }
                val state by viewModel.renderState.collectAsState()
        
                // Similar a onCreate
                LaunchedEffect(Unit) {
                    onScreenComposed(viewModel)
                }
        
                // Similar a onDispose
                DisposableEffect(Unit) {
                    onDispose {
                        onScreenDisposed(viewModel, state)
                    }
                }
        
                buildWidget(viewModel = viewModel, state = state)
            }
        
            @Composable
            abstract fun buildWidget(viewModel: T, state: State)
        
        
            abstract fun namedQualifier() : String
        
            abstract fun onScreenComposed(viewModel: T)
        
            abstract fun onScreenDisposed(viewModel: T, state: State)
        }
    """.trimIndent()
}


class CommonAbstractViewModel(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/ui/screen/AbstractViewModel.kt"
    override val content = """
        package ${info.packageId}.ui.screen

        import androidx.lifecycle.ViewModel
        import kotlinx.coroutines.delay
        import kotlinx.coroutines.flow.MutableStateFlow
        
        interface IViewModel<T>{
            val renderState : MutableStateFlow<T>
            val renderStateValue : T
        }
        
        /**
         * Simple clase q abstrae los métodos más comunes de
         * los viewmodels.
         * Tener
         */
        abstract class AbstractViewModel<State : Any> : ViewModel(), IViewModel<State> {
        
            // StateFlow para mantener el estado actual
            val stateFlow = MutableStateFlow<State>(this.initialState())
            val state = stateFlow.value
        
            // Mét odo abstracto que cada ViewModel debe implementar para definir el estado inicial
            abstract fun initialState(): State
        
            // Mét odo para actualizar el estado de manera atómica, no soporta encolamiento,
            // así q si se envían mensajes demasiado rápidos uno de otros, solo se notificaría el último
            protected fun updateState(state: State) {
                this.stateFlow.value = state
            }
        
            // mé_todo para actualizar el estado con un tiempo delay
            suspend fun updateStateWithDelay(state: State, period : Long = 20L) {
                delay(period)
                this.stateFlow.value = state
            }
        
        }
    """.trimIndent()
}




class CommonCounterScreen(info: ProjectInfo, isOnlyAndroid : Boolean = false) : ProjectFile {
//    override val path = "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/ui/screen/counter/CounterScreen.kt"
    override val path = if(isOnlyAndroid) "${info.moduleName}/src/main/java/${info.packagePath}/ui/screen/counter/CounterScreen.kt" else "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/ui/screen/counter/CounterScreen.kt"
    override val content = """
package ${info.packageId}.ui.screen.counter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ${info.packageId}.di.qualifiers.ServiceQualifier
import ${if(isOnlyAndroid) """com.abexa.shared.screen.AbstractScreen""".trimIndent() else """${info.packageId}.ui.screen.AbstractScreen""".trimIndent()}
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CounterScreenClass : AbstractScreen<CounterViewModel, CounterViewModel.CounterUiState>() {

    @Composable
    override fun buildWidget(viewModel: CounterViewModel, state : CounterViewModel.CounterUiState){
        val coroutineScope = rememberCoroutineScope()

        return Column(modifier = Modifier.padding(16.dp)) {
            LoadingGreet(state.message)
            customComposable(state.message)
            CounterText(state.count)
            LoadingIndicator(state.isLoading)
            CounterButtons(
                onIncrement = {
                    coroutineScope.launch(Dispatchers.IO) { viewModel.increment() }
                },
                onDecrement = {
                    coroutineScope.launch(Dispatchers.IO) { viewModel.decrement() }
                },
                onGreet = {
                    viewModel.greet()
                }
            )
        }
    }

    override fun namedQualifier() : String = ServiceQualifier.ServiceA.value
    override fun onScreenDisposed(
        viewModel: CounterViewModel,
        state: CounterViewModel.CounterUiState
    ) {
        println("onScreenDisposed")
    }

    override fun onScreenComposed(viewModel: CounterViewModel) {
        println("onScreenComposed")
    }

    @Composable
    fun customComposable(name: String) {
        println("customComposable recompuesto")
        Text("Hola, ${'$'}name!")
    }

    @Composable
    fun CounterText(count: Int) {
        println("CounterText recompuesto")
        Text("Counter: ${'$'}count")
    }

    @Composable
    fun LoadingIndicator(isLoading: Boolean) {
        println("LoadingIndicator recompuesto")
        if (isLoading) Text("Cargando...")
    }

    @Composable
    fun LoadingGreet(greet : String) {
        println("LoadingGreet recompuesto")
        Text("greet ${'$'}greet")
    }

    @Composable
    fun CounterButtons(onIncrement: () -> Unit, onDecrement: () -> Unit, onGreet: () -> Unit) {
        println("CounterButtons recompuesto")
        Row {
            Button(onClick = onIncrement, modifier = Modifier.padding(8.dp)) {
                Text("+")
            }
            Button(onClick = onDecrement, modifier = Modifier.padding(8.dp)) {
                Text("-")
            }
            Button(onClick = onGreet, modifier = Modifier.padding(8.dp)) {
                Text("message")
            }
        }
    }

}
    """.trimIndent()
}


class CommonCounterViewModel(info: ProjectInfo, isOnlyAndroid : Boolean = false) : ProjectFile {
//    override val path = "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/ui/screen/counter/CounterViewModel.kt"
    override val path = if(isOnlyAndroid) "${info.moduleName}/src/main/java/${info.packagePath}/ui/screen/counter/CounterViewModel.kt" else "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/ui/screen/counter/CounterViewModel.kt"
    override val content = """
package ${info.packageId}.ui.screen.counter

import ${info.packageId}.data.db.repositories.OrderRepository
import ${info.packageId}.data.network.IdentityServerAPI
import ${info.packageId}.data.spf.IPreferences
import ${info.packageId}.domain.dto.AccessTokenDTO
import ${info.packageId}.domain.dto.AppParamsDTO
import ${info.packageId}.domain.dto.request.TokenRequest
import ${info.packageId}.infrastructure.extensions.safeCallAsPair
//import ${info.packageId}.ui.screen.AbstractViewModel
import ${if(isOnlyAndroid) """com.abexa.shared.screen.AbstractViewModel""".trimIndent() else """${info.packageId}.ui.screen.IViewModel""".trimIndent()}
import ${if(isOnlyAndroid) """com.abexa.shared.screen.IViewModel""".trimIndent() else """${info.packageId}.ui.screen.IViewModel""".trimIndent()}
//import ${info.packageId}.ui.screen.IViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Base64

class CounterViewModel(private val preferences: IPreferences, private val identityServerAPI: IdentityServerAPI, val appParamsDTO : AppParamsDTO, val orderRepository : OrderRepository) : AbstractViewModel<CounterViewModel.CounterUiState>(), IViewModel<CounterViewModel.CounterUiState>{

    override fun initialState(): CounterUiState = CounterUiState()

    override val renderState: MutableStateFlow<CounterUiState>
        get() = stateFlow

    override val renderStateValue: CounterUiState
        get() = renderState.value

    suspend fun increment() {
        appParamsDTO.alias = "${'$'}{System.currentTimeMillis()} A"
        val token = Base64.getEncoder().encodeToString("iGnYoHN2aAwPWTdWrWYkimwJY78a:1PMetOfkOscdVkKi4NIWitqUp7OMmd33nrjsj9DNQgQa".toByteArray())
        val requestBody = TokenRequest(grantType = "password", username = "xtu-mobile-app", password = "xtu-mobile-app", scope = "xtu-mobile.read xtu-mobile.write monitor-logging.read monitor-logging.write profile openid generic-notifier.read generic-notifier.write")
        val (accessToken2, error2) = safeCallAsPair<AccessTokenDTO>{
            identityServerAPI.sendData2(request = requestBody, token = "Basic ${'$'}token")
        }

//        client.sendData2(request = requestBody, contentType = "application/json", token = "Basic ${'$'}token").
        println("token rsponse ${'$'}{accessToken2} ${'$'}error2")

        val users = orderRepository.getAllUsers()
        orderRepository.insertUser("Kane BA", "email${'$'}{System.currentTimeMillis()}@.com")
        val users2 = orderRepository.getAllUsers()
        println("${'$'}users , ${'$'}users2")
//        println("token rsponse ${'$'}{accessToken} ${'$'}error")
//        val response = httpClient.get("characters").body<CharacterResponse>()
        preferences.counter += 1
        super.updateState(renderStateValue.copy(count = preferences.counter))
    }

    fun decrement() {
        preferences.counter -= 1
//        super.updateState(renderStateValue.copy(count = renderStateValue.count - 1))
        super.updateState(renderStateValue.copy(count = preferences.counter))
    }

    fun greet() {
        super.updateState(renderStateValue.copy(message = "M.${'$'}{System.currentTimeMillis()}"))
    }

    data class CounterUiState(
        val count: Int = 0,
        val message: String = "",
        val isLoading: Boolean = false,
        val greet : String = ""
    )
}
    """.trimIndent()
}


class CommonDatabaseDriverFactoryExpect(info: ProjectInfo, isOnlyAndroid : Boolean = false) : ProjectFile {
    override val path = if(isOnlyAndroid) "${info.moduleName}/src/main/java/${info.packagePath}/data/db/DatabaseDriverFactory.kt" else "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/data/db/DatabaseDriverFactory.kt"
    override val content = """
package ${info.packageId}.data.db

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory : DBDriver{
    override fun createDriver(): SqlDriver
}
    """.trimIndent()
}

class CommonDBDriver(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/data/db/DBDriver.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/data/db/DBDriver.kt"
    override val content = """
package ${info.packageId}.data.db

import app.cash.sqldelight.db.SqlDriver

interface DBDriver {
    fun createDriver(): SqlDriver
}
    """.trimIndent()
}

class CommonDBRepository(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/data/db/repositories/OrderRepository.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/data/db/repositories/OrderRepository.kt"

    override val content = """
package ${info.packageId}.data.db.repositories

import ${info.packageId}.db.MyDatabase
import ${info.packageId}.data.db.DBDriver
import ${info.packageId}.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(dbDriver: DBDriver) {
    private val database = MyDatabase(dbDriver.createDriver())

    private val userQueries = database.usersQueries
    private val orderQueries = database.ordersQueries

    suspend fun insertUser(name: String, email: String) = withContext(Dispatchers.IO) {
        userQueries.insertUser(name, email)
    }

    suspend fun getAllUsers(): List<Users> = withContext(Dispatchers.IO) {
        userQueries.listAllUsers().executeAsList()
    }

    suspend fun deleteUser(id: Long) = withContext(Dispatchers.IO) {
        userQueries.deleteUser(id)
    }

    suspend fun updateUser(id: Long, name: String, email: String) = withContext(Dispatchers.IO) {
        userQueries.updateUserById(name, email, id)
    }
}

data class OrderWithUser(val id: Long, val userName: String, val amount: Double)
    """.trimIndent()
}


class CommonOrdersSQLDelight(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/sqldelight/${info.packagePath}/orders.sq"
    else
        "${info.moduleName}/src/commonMain/sqldelight/${info.packagePath}/orders.sq"

    override val content = """
joinOrdersAndUsersByUserId:
SELECT orders.id, users.name, orders.amount
FROM orders
INNER JOIN users ON orders.user_id = users.id;
    """.trimIndent()
}

class CommonUsersSQLDelight(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/sqldelight/${info.packagePath}/users.sq"
    else
        "${info.moduleName}/src/commonMain/sqldelight/${info.packagePath}/users.sq"

    override val content = """
insertUser:
INSERT INTO users (name, email) VALUES (?, ?);
deleteUser:
DELETE FROM users WHERE id = ?;
updateUserById:
UPDATE users SET name = ?, email = ? WHERE id = ?;
listAllUsers:
SELECT * FROM users;
    """.trimIndent()
}

class CommonSchemasSQLDelight(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/sqldelight/${info.packagePath}/schemas.sq"
    else
        "${info.moduleName}/src/commonMain/sqldelight/${info.packagePath}/schemas.sq"
    override val content = """
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL
);

CREATE TABLE orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
    """.trimIndent()
}



// KOIN

class CommonNetworkKoinQualifier(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/di/qualifiers/NetworkQualifier.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/di/qualifiers/NetworkQualifier.kt"

    override val content = """
package ${info.packageId}.di.qualifiers

import org.koin.core.qualifier.named

object NetworkQualifier {
    val GenericKtorfit = named("GenericKtorfit")
    val IdentityServerAPI = named("IdentityServerAPI")
    val AnotherServerAPI = named("AnotherServerAPI")
}
    """.trimIndent()
}


class CommonServiceKoinQualifier(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/di/qualifiers/ServiceQualifier.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/di/qualifiers/ServiceQualifier.kt"

    override val content = """
package ${info.packageId}.di.qualifiers

import org.koin.core.qualifier.named

object ServiceQualifier {
    val ServiceA = named("ServiceA")
    val ServiceB = named("ServiceB")
    val ServiceC = named("ServiceC")
}
    """.trimIndent()
}


class CommonKoinInit(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/di/KoinInit.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/di/KoinInit.kt"

    override val content = """
package ${info.packageId}.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun initKoin(platformModule: Module) {
    startKoin {
        modules(platformModule, serviceModule())
    }
}
    """.trimIndent()
}

class CommonKoinService(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/di/ServiceModule.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/di/ServiceModule.kt"


    override val content = """
package ${info.packageId}.di

import ${info.packageId}.data.db.repositories.OrderRepository
import ${info.packageId}.data.network.AnotherServerAPI
import ${info.packageId}.data.network.IdentityServerAPI

${if(isOnlyAndroid) """
import ${info.packageId}.data.network.createAnotherServerAPI
import ${info.packageId}.data.network.createIdentityServerAPI
""".trimIndent() else 
"""
import ${info.packageId}.data.network.providers.createIdentityServerAPI2
import ${info.packageId}.data.network.providers.createAnotherServerAPI2
import ${info.packageId}.data.spf.provideSettings
""".trimIndent()}

import ${info.packageId}.data.network.providers.HTTPGenericProvider
import ${info.packageId}.data.network.providers.IKtorfitProvider
import ${info.packageId}.data.spf.IPreferences
import ${info.packageId}.data.spf.PreferencesImpl



import ${info.packageId}.di.qualifiers.NetworkQualifier
import ${info.packageId}.di.qualifiers.ServiceQualifier
import ${info.packageId}.domain.dto.AppParamsDTO
import ${info.packageId}.ui.screen.counter.CounterViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun serviceModule(): Module = module {
    single<IPreferences> { PreferencesImpl(provideSettings()) }
    single<AppParamsDTO> { AppParamsDTO(alias = "", isTrackEnabled = false) }

    single<IKtorfitProvider> ( NetworkQualifier.GenericKtorfit ) { HTTPGenericProvider(baseUrl = "https://wso2is-service-7.abexa.pe/", get() ) }
    single<IdentityServerAPI> { get<IKtorfitProvider>(NetworkQualifier.GenericKtorfit).create().createIdentityServerAPI() }
    single<AnotherServerAPI> { get<IKtorfitProvider>(NetworkQualifier.GenericKtorfit).create().createAnotherServerAPI() }

    factory(ServiceQualifier.ServiceA) {
        CounterViewModel(preferences = get(),
            identityServerAPI = get(),
            appParamsDTO = get(),
            orderRepository = OrderRepository(dbDriver = get()) )
    }
}
    """.trimIndent()
}


class OnlyDesktopOrOnlyAndroidKoinService(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/di/ServiceModule.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/di/ServiceModule.kt"

    override val content = """
package ${info.packageId}.di


import ${info.packageId}.AppApplication
import ${info.packageId}.domain.constants.Constants
import ${info.packageId}.data.db.repositories.OrderRepository
import ${info.packageId}.data.network.AnotherServerAPI
import ${info.packageId}.data.network.IdentityServerAPI
import ${info.packageId}.domain.dto.AppParamsDTO
import ${info.packageId}.di.qualifiers.ServiceQualifier
import ${info.packageId}.ui.screen.counter.CounterViewModel

${if(isOnlyAndroid) """
import android.content.Context
import ${info.packageId}.data.network.createAnotherServerAPI
import ${info.packageId}.data.network.createIdentityServerAPI
import com.russhwolf.settings.SharedPreferencesSettings
""".trimIndent() else
"""
import ${info.packageId}.data.network.providers.createIdentityServerAPI2
import ${info.packageId}.data.network.providers.createAnotherServerAPI2
import ${info.packageId}.data.spf.provideSettings
""".trimIndent()}

import ${info.packageId}.data.network.providers.HTTPGenericProvider
import ${info.packageId}.data.network.providers.IKtorfitProvider
import ${info.packageId}.data.spf.IPreferences
import ${info.packageId}.data.spf.PreferencesImpl

import org.koin.core.module.Module
import org.koin.dsl.module

${if(isOnlyAndroid) """
fun serviceModule(): Module = module {
    single<IPreferences> {
        val sharedPreferences = AppApplication.appContext.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE)
        PreferencesImpl(SharedPreferencesSettings(sharedPreferences))
    }

    single<AppParamsDTO> { AppParamsDTO(alias = "", isTrackEnabled = false) }

    single<IdentityServerAPI> {
        HTTPGenericProvider("https://wso2is-service-7.abexa.pe/", get()).create().createIdentityServerAPI()
    }
    single<AnotherServerAPI> {
        HTTPGenericProvider("https://wso2is-service-7.abexa.pe/", get()).create().createAnotherServerAPI()
    }
    
    factory(ServiceQualifier.ServiceA) {
        CounterViewModel(preferences = get(),
            identityServerAPI = get(),
            appParamsDTO = get(),
            orderRepository = OrderRepository(dbDriver = get()) )
    }
}
""" else """
fun serviceModule(): Module = module {
    single<IPreferences> { PreferencesImpl(provideSettings()) }
    single<AppParamsDTO> { AppParamsDTO(alias = "", isTrackEnabled = false) }

    single<IdentityServerAPI> { createIdentityServerAPI2("https://wso2is-service-7.abexa.pe/", get()) }
    single<AnotherServerAPI> { createAnotherServerAPI2("https://wso2is-service-7.abexa.pe/", get()) }
    
    factory(ServiceQualifier.ServiceA) {
        CounterViewModel(preferences = get(),
            identityServerAPI = get(),
            appParamsDTO = get(),
            orderRepository = OrderRepository(dbDriver = get()) )
    }
}
"""}

    """.trimIndent()
}




// com.abexa.kmp.data.network.providers
class CommonHTTPGenericProvider(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/data/network/providers/HTTPGenericProvider.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/data/network/providers/HTTPGenericProvider.kt"

    override val content = """
package ${info.packageId}.data.network.providers

import ${info.packageId}.domain.dto.AppParamsDTO
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

data class NetworkException(val statusCode : Int, val error : String) : Exception("API Error ${'$'}statusCode: ${'$'}error")

interface IKtorfitProvider{
    fun create() : Ktorfit
}

/**
 * Crea un proveedor genérico http envuelto por ktorfit
 */
class HTTPGenericProvider (val baseUrl: String, val appParamsDTO: AppParamsDTO) : IKtorfitProvider{
    override fun create(): Ktorfit = Ktorfit.Builder()
        .baseUrl(baseUrl)
        .httpClient(HttpClient{
            expectSuccess = true // Para que dispare excepciones en 4xx y 5xx
            install(DefaultRequest) {
                header("Content-Type", "application/json")
                header("Custom-Key", appParamsDTO.alias)
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KtorLog => ${'$'}message") // Esto se verá en consola/logcat
                    }
                }
            }
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    if (exception is ResponseException) {
                        val statusCode = exception.response.status.value
                        val errorBody = exception.response.bodyAsText()
                        throw NetworkException(statusCode, errorBody)
                    }
                }
            }
        })
        .build()
}
    """.trimIndent()
}



class OnlyDesktopOrOnlyAndroidHTTPGenericProvider(info: ProjectInfo, isAndroid : Boolean = false) : ProjectFile {
    override val path = if(isAndroid) "${info.moduleName}/src/androidMain/kotlin/${info.packagePath}/data/network/providers/HTTPGenericProvider.android.kt" else "${info.moduleName}/src/desktopMain/kotlin/${info.packagePath}/data/network/providers/HTTPGenericProvider.desktop.kt"

    override val content = """
package ${info.packageId}.data.network.providers

import ${info.packageId}.data.network.AnotherServerAPI
import ${info.packageId}.data.network.IdentityServerAPI
import ${info.packageId}.data.network.createAnotherServerAPI
import ${info.packageId}.data.network.createIdentityServerAPI
import ${info.packageId}.domain.dto.AppParamsDTO

actual fun createAnotherServerAPI2(baseUrl : String, appParamsDTO: AppParamsDTO): AnotherServerAPI{
    return HTTPGenericProvider(baseUrl, appParamsDTO).create().createAnotherServerAPI()
}
actual fun createIdentityServerAPI2(baseUrl : String, appParamsDTO: AppParamsDTO): IdentityServerAPI{
    return HTTPGenericProvider(baseUrl, appParamsDTO).create().createIdentityServerAPI()
}
    """.trimIndent()
}


class OnlyAndroidHTTPGenericProvider(info: ProjectInfo) : ProjectFile {
    override val path = "${info.moduleName}/src/androidMain/kotlin/${info.packagePath}/data/network/providers/HTTPGenericProvider.android.kt"

    override val content = """
package ${info.packageId}.data.network.providers

import ${info.packageId}.data.network.AnotherServerAPI
import ${info.packageId}.data.network.IdentityServerAPI
import ${info.packageId}.data.network.createAnotherServerAPI
import ${info.packageId}.data.network.createIdentityServerAPI
import ${info.packageId}.domain.dto.AppParamsDTO

actual fun createAnotherServerAPI2(baseUrl : String, appParamsDTO: AppParamsDTO): AnotherServerAPI{
    return HTTPGenericProvider(baseUrl, appParamsDTO).create().createAnotherServerAPI()
}
actual fun createIdentityServerAPI2(baseUrl : String, appParamsDTO: AppParamsDTO): IdentityServerAPI{
    return HTTPGenericProvider(baseUrl, appParamsDTO).create().createIdentityServerAPI()
}
    """.trimIndent()
}

// com.abexa.kmp.data.network.providers
class OnlyDesktopOrOnlyAndroidCommonHTTPGenericProvider(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/data/network/providers/HTTPGenericProvider.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/data/network/providers/HTTPGenericProvider.kt"

    override val content = """
package ${info.packageId}.data.network.providers

import ${info.packageId}.data.network.AnotherServerAPI
import ${info.packageId}.data.network.IdentityServerAPI
import ${info.packageId}.domain.dto.AppParamsDTO
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

data class NetworkException(val statusCode : Int, val error : String) : Exception("API Error ${'$'}statusCode: ${'$'}error")

interface IKtorfitProvider{
    fun create() : Ktorfit
}

${if(isOnlyAndroid) "" else "expect fun createAnotherServerAPI2(baseUrl : String, appParamsDTO: AppParamsDTO): AnotherServerAPI"}
${if(isOnlyAndroid) "" else "expect fun createIdentityServerAPI2(baseUrl : String, appParamsDTO: AppParamsDTO): IdentityServerAPI"}

/**
 * Crea un proveedor genérico http envuelto por ktorfit
 */
class HTTPGenericProvider (val baseUrl: String, val appParamsDTO: AppParamsDTO) : IKtorfitProvider{
    override fun create(): Ktorfit = Ktorfit.Builder()
        .baseUrl(baseUrl)
        .httpClient(HttpClient{
            expectSuccess = true // Para que dispare excepciones en 4xx y 5xx
            install(DefaultRequest) {
                header("Content-Type", "application/json")
                header("Custom-Key", appParamsDTO.alias)
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KtorLog => ${'$'}message") // Esto se verá en consola/logcat
                    }
                }
            }
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    if (exception is ResponseException) {
                        val statusCode = exception.response.status.value
                        val errorBody = exception.response.bodyAsText()
                        throw NetworkException(statusCode, errorBody)
                    }
                }
            }
        })
        .build()
}
    """.trimIndent()
}

class CommonPreferencesSPF(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/data/spf/Preferences.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/data/spf/Preferences.kt"

    override val content = """
package ${info.packageId}.data.spf

import ${info.packageId}.domain.dto.AppParamsDTO
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.russhwolf.settings.get
import kotlinx.serialization.json.Json

interface IPreferences{
    var isGrantedPermissions : Boolean
    var appParamsDTO : AppParamsDTO
    var counter : Int
}

inline fun <reified T> decode(preferencesKey : String, settings: Settings): T? {
    return settings[preferencesKey]
}

inline fun <reified T> jsonDecode(preferencesKey : String, settings: Settings): T? {
    val contentJson = decode(preferencesKey, settings) as String?
    if(contentJson != null)
        return Json.decodeFromString<T>(contentJson)
    return null
}

inline fun <reified T> jsonEncode(content : T): String = Json.encodeToString(content)

class PreferencesImpl(private val settings : Settings) : IPreferences{

    override var isGrantedPermissions: Boolean
        get() = settings[PreferencesImpl.KEY_IS_GRANTED_PERMISSIONS] ?: false
        set(value) { settings[PreferencesImpl.KEY_IS_GRANTED_PERMISSIONS] = value }

    override var counter: Int
        get() = settings[PreferencesImpl.KEY_COUNTER] ?: 0
        set(value) { settings[PreferencesImpl.KEY_COUNTER] = value }
    
    override var appParamsDTO: AppParamsDTO
        get() = jsonDecode<AppParamsDTO>(PreferencesImpl.KEY_APP_PARAMS, settings) ?: AppParamsDTO(alias = "", isTrackEnabled = false)
        set(value) { settings[PreferencesImpl.KEY_APP_PARAMS] = jsonEncode(value) }

    companion object {
        private const val KEY_IS_GRANTED_PERMISSIONS = "KEY_IS_GRANTED_PERMISSIONS"
        private const val KEY_APP_PARAMS = "KEY_APP_PARAMS"
        private const val KEY_COUNTER = "KEY_COUNTER"
    }
}
    """.trimIndent()
}


class CommonPreferencesConfig(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/data/spf/PreferencesConfig.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/data/spf/PreferencesConfig.kt"

    override val content = """
package ${info.packageId}.data.spf

import com.russhwolf.settings.Settings

expect fun provideSettings(): Settings
""".trimIndent()
}



class CommonNetworkAnotherServerAPI(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/data/network/AnotherServerAPI.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/data/network/AnotherServerAPI.kt"

    override val content = """
package ${info.packageId}.data.network

import ${info.packageId}.domain.dto.AccessTokenDTO
import ${info.packageId}.domain.dto.request.TokenRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.HttpResponse


// https://medium.com/@santimattius/from-retrofit-to-ktorfit-on-the-way-to-kotlin-multiplatform-eebfa81f87ed
interface AnotherServerAPI {

    @POST("oauth2/token")
    suspend fun sendData(
        @Header("Authorization") token : String? = "",
//        @Header("Content-Type") contentType: String? = "application/json", // Especificamos Content-Type como JSON
        @Body request: TokenRequest
    ): AccessTokenDTO


    @POST("oauth2/token")
    suspend fun sendData2(
        @Header("Authorization") token : String? = "",
        @Body request: TokenRequest
    ): HttpResponse
}
""".trimIndent()
}


class CommonNetworkIdentityServerAPI(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/data/network/IdentityServerAPI.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/data/network/IdentityServerAPI.kt"
    override val content = """
package ${info.packageId}.data.network

import ${info.packageId}.domain.dto.AccessTokenDTO
import ${info.packageId}.domain.dto.request.TokenRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.HttpResponse


// https://medium.com/@santimattius/from-retrofit-to-ktorfit-on-the-way-to-kotlin-multiplatform-eebfa81f87ed
interface IdentityServerAPI {

    @POST("oauth2/token")
    suspend fun sendData(
        @Header("Authorization") token : String? = "",
//        @Header("Content-Type") contentType: String? = "application/json", // Especificamos Content-Type como JSON
        @Body request: TokenRequest
    ): AccessTokenDTO


    @POST("oauth2/token")
    suspend fun sendData2(
        @Header("Authorization") token : String? = "",
        @Body request: TokenRequest
    ): HttpResponse
}
""".trimIndent()
}



class CommonConstants(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid) "${info.moduleName}/src/main/java/${info.packagePath}/domain/constants/Constants.kt"
    else "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/domain/constants/Constants.kt"

    override val content = """
package ${info.packageId}.domain.constants

object Constants {
    const val PREFERENCES_NAME = "app-mobile-template-spf-name.md"
}
""".trimIndent()
}


class CommonRequestTokenDTO(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/domain/dto/request/TokenRequest.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/domain/dto/request/TokenRequest.kt"
    override val content = """
package ${info.packageId}.domain.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    @SerialName("grant_type")
    val grantType: String,
    @SerialName("username")
    val username: String,
    @SerialName("scope")
    val scope: String,
    @SerialName("password")
    val password: String
)
""".trimIndent()
}


class CommonAccessTokenDTO(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/domain/dto/AccessTokenDTO.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/domain/dto/AccessTokenDTO.kt"

    override val content = """
package ${info.packageId}.domain.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenDTO (
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    val scope: String,
    @SerialName("id_token")
    val idToken: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("expires_in")
    val expiresIn: Long
)
""".trimIndent()
}

class CommonAppParamsDTO(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/domain/dto/AppParamsDTO.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/domain/dto/AppParamsDTO.kt"

    override val content = """
package ${info.packageId}.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppParamsDTO(
    var alias : String,
    var isTrackEnabled : Boolean
)
""".trimIndent()
}


class CommonExtensions(info: ProjectInfo, isOnlyAndroid: Boolean = false) : ProjectFile {
    override val path = if (isOnlyAndroid)
        "${info.moduleName}/src/main/java/${info.packagePath}/infrastructure/extensions/Extensions.kt"
    else
        "${info.moduleName}/src/commonMain/kotlin/${info.packagePath}/infrastructure/extensions/Extensions.kt"

    override val content = """
package ${info.packageId}.infrastructure.extensions


import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

data class ExceptionResponse(
    val errorCode : Int,
    val errorBody : String
)

/**
 * Parsea un objeto HttpResponse devolviendo el
 * objeto de negocio o la respuesta del error body.
 *
    @POST("oauth2/token")
    suspend fun sendData(): HttpResponse
 *
    val (accessToken, error) = safeCallAsPair {
        client.sendData()
    }
 */
suspend inline fun <reified T> safeCallAsPair(
    crossinline apiCall: suspend () -> HttpResponse
): Pair<T?, Pair<Int, String>?> {
    return try {
        val response = apiCall()
        if (response.status.value in 200..299) {
            val body = response.body<T>()
            Pair(body, null)
        } else {
            val errorBody = response.bodyAsText()
            Pair(null, Pair(response.status.value, errorBody))
        }
    } catch (e: Exception) {
        Pair(null, Pair(-1, e.localizedMessage ?: "Unknown error"))
    }
}
""".trimIndent()
}


