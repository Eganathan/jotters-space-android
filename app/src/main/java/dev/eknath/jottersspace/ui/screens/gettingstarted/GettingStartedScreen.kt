package dev.eknath.jottersspace.ui.screens.gettingstarted

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.eknath.jottersspace.MainActivity
import dev.eknath.jottersspace.R
import dev.eknath.jottersspace.ui.navigation.AppNavSpec
import dev.eknath.jottersspace.zCatalystSDK.ZAuthSDK


@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textRes: Int,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedOffsetY by animateDpAsState(
        targetValue = if (isPressed) 15.dp else 0.dp,
        label = "pressed-animation"
    )

    Button(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(60.dp)
            .offset { IntOffset(0, animatedOffsetY.value.toInt()) },
        enabled = enabled,
        interactionSource = interactionSource,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f),
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Spacer(height: Dp = 5.dp, width: Dp = 5.dp) {
    Spacer(Modifier.size(height = height, width = width))
}

@Composable
fun GettingStartedScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val currentUser = ZAuthSDK.currentUser.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.intro_jotterspace),
                contentDescription = "Jotters Space logo"
            )
            Spacer(height = 200.dp)
            /*PrimaryButton(
                textRes = R.string.google_login,
                onClick = {
                    ZAuthSDK.googleLogin(
                        activity = navController.context as MainActivity,
                        onLoad = { isLoading = true },
                        onSuccess = {
                            isLoading = false
                            navController.navigate(AppNavSpec.Home(userName = ""))
                        },
                        onFailure = {
                            isLoading = false
                        }
                    )
                }
            )*/
            PrimaryButton(
                textRes = R.string.login,
                onClick = {
                    ZAuthSDK.initiateUserLogin(
                        onLoad = { isLoading = true },
                        onSuccess = {
                            isLoading = false
                            navController.navigate(AppNavSpec.Home(userName = ""))
                        },
                        onFailure = {
                            isLoading = false
                        }
                    )
                }
            )
            Spacer(height = 10.dp)
            PrimaryButton(
                textRes = R.string.signup,
                onClick = {
                    navController.navigate(AppNavSpec.SignUpScreen)
                }
            )
        }

        AnimatedVisibility(isLoading, enter = fadeIn(), exit = fadeOut()) {
            LoadingDialog()
        }
    }
}

@Composable
fun LoadingDialog(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
                Spacer(height = 10.dp)
                Text("Loading...")
            }
        }
    }
}