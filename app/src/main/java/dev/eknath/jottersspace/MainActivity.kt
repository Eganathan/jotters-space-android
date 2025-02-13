package dev.eknath.jottersspace

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.eknath.jottersspace.ui.navigation.AppNav
import dev.eknath.jottersspace.ui.theme.JottersSpaceTheme
import dev.eknath.jottersspace.zCatalystSDK.ZAuthSDK

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZAuthSDK.initialize(this)
        enableEdgeToEdge()
        setContent {
            JottersSpaceTheme {
                AppNav()
            }
        }
    }
}

fun Context.getShortcutCode(): Int {
    val shortCutCode = (this as MainActivity).intent.getStringExtra("content_key")?.takeIf { it.isNotBlank() && it.startsWith("_ssKey_") } ?: return 0
    return when (shortCutCode) {
        "_ssKey_create_note" -> 1
        else -> 0
    }
}