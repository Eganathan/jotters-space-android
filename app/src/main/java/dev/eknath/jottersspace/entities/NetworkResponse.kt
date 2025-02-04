package dev.eknath.jottersspace.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApiResponse(
    @SerializedName("data") val data: Data,
    @SerializedName("status") val status: String
):Serializable

data class Data(
    @SerializedName("paging_info") val pagingInfoWrapper: PagingInfoWrapper,
    @SerializedName("jots") val jots: List<Jot>
)

data class PagingInfoWrapper(
    @SerializedName("paging_info") val pagingInfo: PagingInfo
)

//data class PagingInfo(
//    @SerializedName("per_page") val perPage: Int,
//    @SerializedName("page") val page: Int,
//    @SerializedName("has_more") val hasMore: Boolean
//)

data class Jot(
    @SerializedName("created_time") val createdTime: String,
    @SerializedName("note") val note: String,
    @SerializedName("modified_time") val modifiedTime: String,
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String
)
