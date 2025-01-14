package dev.eknath.jottersspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import dev.eknath.jottersspace.auth.ZAuthSDK
import dev.eknath.jottersspace.ui.theme.JottersSpaceTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZAuthSDK.initialize(this)

        enableEdgeToEdge()
        setContent {
            JottersSpaceTheme {
                GettingStartedScreen(modifier = Modifier)
            }
        }
    }
}

@Composable
fun GettingStartedScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val currentUser = ZAuthSDK.currentUser.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("name:"+ currentUser.value?.email)


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