package dev.eknath.jottersspace.ui.screens.appswitchers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import dev.eknath.jottersspace.MainActivity
import dev.eknath.jottersspace.ui.navigation.AppNavSpec
import dev.eknath.jottersspace.zCatalystSDK.ZAuthSDK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun AppSwitcherScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val activity = (LocalContext.current as MainActivity)
    val shortCutCode = (if(activity.intent.getStringExtra("content_key")?.contains("create_note") == true) 1 else 0)

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Please Wait...")
    }

    LaunchedEffect(Unit) {
        scope.launch {
            val currentUser = async { ZAuthSDK.getCurrentUser() }.await()
            if (currentUser != null) {
                navController.navigate(
                    AppNavSpec.Home(
                        userName = currentUser.firstName,
                        shortCutCode = shortCutCode,
                    )
                )
            } else {
                navController.navigate(AppNavSpec.GettingStarted)
            }
        }
    }
}