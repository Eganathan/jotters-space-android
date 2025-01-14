package dev.eknath.jottersspace.ui.screens.appswitchers

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dev.eknath.jottersspace.auth.ZAuthSDK
import dev.eknath.jottersspace.ui.navigation.AppNavSpec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun AppSwitcherScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    Text("Loading AppSwitcherScreen")


    LaunchedEffect(Unit) {
        scope.launch {
            val currentUser = async { ZAuthSDK.getCurrentUser() }.await()
            if (currentUser != null) {
                navController.navigate(
                    AppNavSpec.Home(
                        userName = currentUser.firstName
                    )
                )
            } else {
                navController.navigate(AppNavSpec.GettingStarted)
            }
        }
    }
}