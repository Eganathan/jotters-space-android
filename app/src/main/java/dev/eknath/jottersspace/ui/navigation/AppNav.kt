package dev.eknath.jottersspace.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.eknath.jottersspace.ui.screens.appswitchers.AppSwitcherScreen
import dev.eknath.jottersspace.ui.screens.auth.EmailVerificationScreen
import dev.eknath.jottersspace.ui.screens.auth.SignUpScreen
import dev.eknath.jottersspace.ui.screens.gettingstarted.GettingStartedScreen
import dev.eknath.jottersspace.ui.screens.home.HomeScreen
import dev.eknath.jottersspace.zCatalystSDK.ZAuthSDK
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppNavSpec {

    @Serializable
    data object AppSwitcher : AppNavSpec

    @Serializable
    data object SignUpScreen : AppNavSpec

    @Serializable
    data class EmailVerificationScreen(val email: String, val name: String) : AppNavSpec

    @Serializable
    data object GettingStarted : AppNavSpec

    @Serializable
    data class Home(val userName: String) : AppNavSpec
}

@Composable
fun AppNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (ZAuthSDK.isUserSignedIn()) AppNavSpec.AppSwitcher else AppNavSpec.GettingStarted,
    ) {
        composable<AppNavSpec.AppSwitcher> {
            AppSwitcherScreen(navController = navController)
        }

        composable<AppNavSpec.GettingStarted> {
            GettingStartedScreen(navController)
        }

        composable<AppNavSpec.SignUpScreen> {
            SignUpScreen(navController = navController)
        }

        composable<AppNavSpec.EmailVerificationScreen> {
            val emailVerificationParams = it.toRoute<AppNavSpec.EmailVerificationScreen>()
            EmailVerificationScreen(
                navController = navController,
                name = emailVerificationParams.name,
                email = emailVerificationParams.email
            )
        }

        composable<AppNavSpec.Home> {
            val homeParams = it.toRoute<AppNavSpec.Home>()
            HomeScreen(navController = navController, name = homeParams.userName)
        }
    }
}
