package dev.eknath.jottersspace.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.eknath.jottersspace.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleStringRes: Int,
    navigation: @Composable () -> Unit = {},
    actions: @Composable() RowScope.() -> Unit = {},
    isLoading: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopAppBar(
            modifier = modifier,
            title = { Text(stringResource(titleStringRes)) },
            navigationIcon = navigation,
            actions = actions
        )
        if (isLoading)
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
            )
    }
}

@Composable
fun DefaultBackButton(onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    DefaultIconButton(
        iconRes = R.drawable.ic_back,
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
fun DefaultIconButton(
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = "back",
        )
    }
}