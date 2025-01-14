package dev.eknath.jottersspace.auth

import android.content.Context
import android.util.Log
import com.zoho.catalyst.org.ZCatalystUser
import com.zoho.catalyst.setup.ZCatalystApp
import com.zoho.catalyst.setup.ZCatalystSDKConfigs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object ZAuthSDK {
    private lateinit var catalystSDK: ZCatalystApp
    private var isInitialized = false

    private val _currentUser: MutableStateFlow<ZCatalystUser?> = MutableStateFlow(null)
    val currentUser = _currentUser.asStateFlow()


    fun initialize(context: Context) {
        catalystSDK = ZCatalystApp.init(
            context = context,
            environment = ZCatalystSDKConfigs.Environment.DEVELOPMENT
        )
        isInitialized = true

    }

    suspend fun initiateUserLogin(): Boolean {
        return suspendCoroutine { cont ->
            catalystSDK.login(
                customParams = hashMapOf(),
                success = {
                    cont.resume(true)
                }, failure = {
                    cont.resume(false)
                }
            )
        }
    }

    suspend fun initiateUserSignUp(name: String, email: String): Boolean {
        return suspendCoroutine { cont ->
            catalystSDK.signUp(
                newUser = catalystSDK.newUser(firstName = name, email = email),
                success = {
                    cont.resume(true)
                }, failure = {
                    cont.resume(false)
                }
            )
        }
    }

    fun isUserSignedIn(): Boolean = isInitialized && catalystSDK.isUserSignedIn()

    suspend fun getCurrentUser(): ZCatalystUser? {
        return suspendCoroutine { cont ->
            catalystSDK.getCurrentUser(
                success = { zUser ->
                    _currentUser.value = zUser
                    cont.resume(zUser)
                    Log.e("Test", "User:S")
                }, failure = {
                    _currentUser.value = null
                    cont.resume(null)
                    Log.e("Test", "User:F")
                })
        }
    }

}