package dev.eknath.jottersspace.network

import dev.eknath.jottersspace.zCatalystSDK.ZAuthSDK
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        runBlocking {
            ZAuthSDK.getToken()
        }?.let { token ->
            builder.addHeader(
                "Authorization",
                "Bearer $token"
            )
        }
        return chain.proceed(builder.build())
    }
}

