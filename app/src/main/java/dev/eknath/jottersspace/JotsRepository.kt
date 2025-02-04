package dev.eknath.jottersspace

import dev.eknath.jottersspace.entities.ApiResponse
import dev.eknath.jottersspace.network.JotsApiService
import javax.inject.Inject

interface JotsRepository {
    suspend fun getBulkJots(): ApiResponse?
}

class JotsRepositoryImpl @Inject constructor(
    private val apiService: JotsApiService
) : JotsRepository {
    override suspend fun getBulkJots(): ApiResponse? {
        val response = apiService.getBulkJots()
        return if (response.isSuccessful) response.body() else null
    }
}
