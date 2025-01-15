package dev.eknath.jottersspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.eknath.jottersspace.zCatalystSDK.ZAuthSDK
import dev.eknath.jottersspace.ui.navigation.AppNav
import dev.eknath.jottersspace.ui.theme.JottersSpaceTheme

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