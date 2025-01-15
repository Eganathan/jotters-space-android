package dev.eknath.jottersspace.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BulkJotResponse(
    val data: List<JotNote>,
    val info: PagingInfo,
    val status: Status
)

@Serializable
data class Status(val code: Int, val message: String)

@Serializable
data class PagingInfo(
    val hasMoreData: Boolean,
    val totalRecords: Int,
)


@Serializable
data class JotNote(
    @SerialName("ROWID") val id: Long = 0L,
    @SerialName("CREATORID") val creatorId: Long = 0L,
    val title: String,
    val note: String,
    @SerialName("CREATEDTIME") val createdTime: String = "",
    @SerialName("MODIFIEDTIME") val updatedTime: String = "",
    @SerialName("is_deleted") val isDeleted: Boolean = false
)
