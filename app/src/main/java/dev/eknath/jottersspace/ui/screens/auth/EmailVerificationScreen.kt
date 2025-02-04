package dev.eknath.jottersspace.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.eknath.jottersspace.R
import dev.eknath.jottersspace.ui.components.DefaultBackButton
import dev.eknath.jottersspace.ui.components.DefaultTopAppBar
import dev.eknath.jottersspace.ui.navigation.AppNavSpec
import dev.eknath.jottersspace.ui.screens.gettingstarted.PrimaryButton
import dev.eknath.jottersspace.ui.screens.gettingstarted.Spacer
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    email: String,
    name: String,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            DefaultTopAppBar(
                titleStringRes = R.string.empty_string,
                navigation = {
                    DefaultBackButton(
                        enabled = true,
                        onClick = {
                            navController.navigateUp()
                        }
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.intro_jotterspace),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(0.6f)
                    .fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .padding(top = 90.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween

            ) {
                Column {
                    Text(
                        text = "Account Created Successfully!",
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Text(
                        text = "Kindly check your email instruction for verification and setting up a secure password",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Spacer(height = 100.dp)
                }
                Column(modifier = Modifier.padding(bottom = 20.dp)) {

                    PrimaryButton(
                        textRes = R.string.proceed,
                        enabled = !isLoading
                    ) {
                        navController.navigate(AppNavSpec.GettingStarted) {
                            launchSingleTop = true
                        }
                    }
                }
            }
        }

    }
}