package dev.eknath.jottersspace.ui.screens.appswitchers

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dev.eknath.jottersspace.auth.ZAuthSDK
import dev.eknath.jottersspace.ui.navigation.AppNavSpec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppSwitcherScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val currentUser = ZAuthSDK.currentUser.collectAsState()

    Text("Loading AppSwitcherScreen")
    

    LaunchedEffect(currentUser.value) {
        if (currentUser.value != null)
            navController.navigate(AppNavSpec.Home(userName = currentUser.value?.firstName ?: ""))
        else
            scope.launch {
                if (ZAuthSDK.getCurrentUser() != null) {
                    navController.navigate(
                        AppNavSpec.Home(
                            userName = currentUser.value?.firstName ?: ""
                        )
                    )
                } else {
                    navController.navigate(AppNavSpec.GettingStarted)
                }
            }
    }
}