package dev.eknath.jottersspace.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.eknath.jottersspace.auth.ZAuthSDK
import dev.eknath.jottersspace.ui.screens.appswitchers.AppSwitcherScreen
import dev.eknath.jottersspace.ui.screens.gettingstarted.GettingStartedScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppNavSpec {

    @Serializable
    data object AppSwitcher : AppNavSpec

    @Serializable
    data object GettingStarted : AppNavSpec

}

@Composable
fun AppNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentUser = ZAuthSDK.currentUser.collectAsState()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (currentUser.value == null) AppNavSpec.GettingStarted else AppNavSpec.AppSwitcher,
    ) {
        composable<AppNavSpec.AppSwitcher> {
            AppSwitcherScreen(navController = navController)
        }

        composable<AppNavSpec.GettingStarted> {
            GettingStartedScreen(navController)
        }
    }
}
