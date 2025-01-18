package dev.eknath.jottersspace.ui.screens.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zoho.catalyst.org.ZCatalystUser
import dev.eknath.jottersspace.R
import dev.eknath.jottersspace.ui.components.DefaultBackButton
import dev.eknath.jottersspace.ui.components.DefaultTopAppBar
import dev.eknath.jottersspace.ui.screens.gettingstarted.PrimaryButton
import dev.eknath.jottersspace.ui.screens.gettingstarted.Spacer
import dev.eknath.jottersspace.zCatalystSDK.ZAuthSDK
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val emailRegex = Regex("^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
    var nameTextField by remember { mutableStateOf(TextFieldValue("")) }
    var emailTextField by remember { mutableStateOf(TextFieldValue("")) }
    var isInvited by remember { mutableStateOf(false) }
    var currentUser by remember { mutableStateOf<ZCatalystUser?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val isValidEmail by remember { derivedStateOf { emailTextField.text.isNotEmpty() && emailRegex.containsMatchIn(emailTextField.text)  } }
    val isValidName by remember { derivedStateOf { nameTextField.text.isNotBlank()  } }

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
                        text = stringResource(R.string.account_creation),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Spacer(height = 5.dp)

                    DefaultTextField(
                        modifier = Modifier.fillMaxWidth(),
                        labelRes = R.string.name,
                        value = nameTextField,
                        onValueChange = {
                            nameTextField = it
                        },
                    )
                    Spacer(height = 30.dp)
                    DefaultTextField(
                        modifier = Modifier.fillMaxWidth(),
                        labelRes = R.string.email,
                        value = emailTextField,
                        onValueChange = {
                            emailTextField = it
                        },
                    )
                    Spacer(height = 100.dp)
                }
                Column(modifier = Modifier.padding(bottom = 20.dp)) {
                    Text(
                        text = "By Clicking `Sign Up` you acknowledge that you have gone through the terms of service &  privacy policy of ours & our service providers that is Zoho Catalyst.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    PrimaryButton(
                        textRes = R.string.confirm,
                        enabled = (isValidName && isValidEmail)
                    ) {
                        isLoading = true
                        ZAuthSDK.initiateUserSignUp(
                            name = nameTextField.text,
                            email = emailTextField.text,
                            onSuccess = {
                                isInvited = true
                                isLoading = false
                            },
                            onError = {

                                isLoading = false
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isError: Boolean = false,
    singleLine: Boolean = true,
    @StringRes placeHolderRes: Int = R.string.empty_string,
    @StringRes labelRes: Int = R.string.empty_string,
    includeClearOptions: Boolean = true,
    interactionState: MutableInteractionSource = MutableInteractionSource()
) {
    val isFocused = interactionState.collectIsFocusedAsState()

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        interactionSource = interactionState,
        placeholder = { Text(stringResource(placeHolderRes)) },
        label = { Text(stringResource(labelRes)) },
        isError = isError,
        shape = CircleShape,
        singleLine = singleLine,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background
        )
    )

}