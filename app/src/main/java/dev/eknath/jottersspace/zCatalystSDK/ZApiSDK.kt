package dev.eknath.jottersspace.zCatalystSDK

import com.zoho.catalyst.common.ResponseInfo
import com.zoho.catalyst.datastore.ZCatalystDataStore
import com.zoho.catalyst.datastore.ZCatalystRow
import com.zoho.catalyst.datastore.ZCatalystTable
import com.zoho.catalyst.filestore.ZCatalystFileStore
import com.zoho.catalyst.org.ZCatalystUser
import com.zoho.catalyst.setup.ZCatalystApp
import dev.eknath.jottersspace.entities.BulkJotResponse
import dev.eknath.jottersspace.entities.JotNote
import dev.eknath.jottersspace.entities.PagingInfo
import dev.eknath.jottersspace.entities.Status
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ZCatalystConstants {

    object zTableIds {
        const val JotsTableId = 11585000000048001L

        const val LinksTableId = 11585000000216013L
        const val LinksCollectionTableId = 11585000000118753
    }
}
typealias TABLE_IDS = ZCatalystConstants.zTableIds


class ZApiSDK(private val zCatalystApp: ZCatalystApp) {

    private val zFileStore: ZCatalystFileStore = zCatalystApp.getFileStoreInstance()
    private val zDataStore: ZCatalystDataStore = zCatalystApp.getDataStoreInstance()

    private val zJotsTable = zDataStore.getTableInstance(TABLE_IDS.JotsTableId)

    private val zLinksTable = zDataStore.getTableInstance(TABLE_IDS.LinksTableId)
    private val zLinkCollectionsTable =
        zDataStore.getTableInstance(TABLE_IDS.LinksCollectionTableId)


    suspend fun fetchUserDetails(): ZCatalystUser? {
        return suspendCoroutine { cont ->
            zCatalystApp.getCurrentUser(
                success = { zUser ->
                    cont.resume(zUser)
                }, failure = {
                    cont.resume(null)
                }
            )
        }
    }

    suspend fun getBulkJots(
        onSuccess: (BulkJotResponse) -> Unit,
        onFailure: () -> Unit
    ) {
        zJotsTable.getRows(
            maxRows = 100,
            success = { data, info ->
                onSuccess(
                    BulkJotResponse(
                        data = data.toJotNotes(),
                        info = info.toPagingInfo(),
                        status = if (data.isNotEmpty()) Status(200, "Success") else Status(
                            204,
                            "Empty"
                        )
                    )
                )
            },
            failure = { exception ->
                onFailure()
            }
        )
    }

    fun createNewJot(
        data: JotNote,
        onSuccess: (JotNote) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val row = data.getRowInstance(zJotsTable)
        row.create(
            success = {
                onSuccess(it.toJotNote())
            },
            failure = { exception ->
                onFailure(exception)
            }
        )
    }

    fun updateJot(
        data: JotNote,
        onSuccess: (JotNote) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val row = data.getRowInstance(zJotsTable)
        row.update(
            success = {
                onSuccess(it.toJotNote())
            },
            failure = { exception ->
                onFailure(exception)
            }
        )
    }
}

internal fun ResponseInfo.toPagingInfo() = PagingInfo(
    hasMoreData = this.moreRecords,
    totalRecords = this.totalRecords ?: 0
)


internal fun List<ZCatalystRow>.toJotNotes(): List<JotNote> {
    return map { it.toJotNote() }
}

internal fun ZCatalystRow.toJotNote(): JotNote {
    return JotNote(
        id = id,
        creatorId = creatorId,
        title = getFieldValue("title") as String,
        note = getFieldValue("note") as String,
        createdTime = createdTime,
        updatedTime = modifiedTime,
        isDeleted = getFieldValue("is_deleted") as String == "true"
    )
}


internal fun JotNote.getRowInstance(table: ZCatalystTable): ZCatalystRow {
    val row = table.newRow()
    if (this.id != 0L)
        row.setColumnValue("ROWID", id)

    row.setColumnValue("title", title)
    row.setColumnValue("note", note)
    row.setColumnValue("is_deleted", isDeleted.toString())
    return row
}

//sealed class ResultStatus<out T> {
//    data class Success<out T>(val data: T) : ResultStatus<T>()
//    data class Error(val text: String, val code: Int) : ResultStatus<Nothing>()
//}