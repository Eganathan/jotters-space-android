package dev.eknath.jottersspace.ui.screens.gettingstarted

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dev.eknath.jottersspace.zCatalystSDK.ZAuthSDK
import kotlinx.coroutines.launch

@Composable
fun GettingStartedScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val currentUser = ZAuthSDK.currentUser.collectAsState()

    Text("Getting Started Screen")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("name:" + currentUser.value?.email)


        Button(
            onClick = {
                scope.launch {
                    ZAuthSDK.getCurrentUser()
                }
            },
            content = {
                Text(text = "re fetch user")
            },
        )


        Button(
            onClick = {
                scope.launch {
                    ZAuthSDK.initiateUserLogin()
                }
            },
            content = {
                Text(text = "Login")
            },
        )

        Button(
            onClick = {
                scope.launch {
                    ZAuthSDK.initiateUserSignUp(
                        name = "text-eknath",
                        email = "mail@eknath.dev"
                    )
                }
            },
            content = {
                Text(text = "Sign Up")
            },
        )
    }
}