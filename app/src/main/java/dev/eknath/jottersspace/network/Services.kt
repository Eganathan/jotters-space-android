package dev.eknath.jottersspace.network

import dev.eknath.jottersspace.entities.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JotsApiService {

    @GET("jots")
    suspend fun getBulkJots(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 100,
    ): Response<ApiResponse>

}
