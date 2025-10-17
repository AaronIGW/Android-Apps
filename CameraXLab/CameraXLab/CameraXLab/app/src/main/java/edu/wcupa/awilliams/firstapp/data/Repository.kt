package edu.wcupa.awilliams.firstapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import edu.wcupa.awilliams.firstapp.data.db.AppDatabase
import edu.wcupa.awilliams.firstapp.data.db.IdeaDao
import edu.wcupa.awilliams.firstapp.data.db.IdeaEntity
import edu.wcupa.awilliams.firstapp.data.db.ShotDao
import edu.wcupa.awilliams.firstapp.data.db.ShotEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(
    private val db: AppDatabase,
    private val ideaDao: IdeaDao,
    private val shotDao: ShotDao
) {
    fun ideasPaged(q: String, tag: String, sort: String): Flow<PagingData<IdeaEntity>> =
        Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false)) {
            ideaDao.paging(q, tag, sort)
        }.flow

    fun tags(): Flow<List<String>> =
        ideaDao.allTagsCsv().map { rows ->
            val flat = rows.flatMap { it.split(",") }.map { it.trim() }.filter { it.isNotEmpty() }
            listOf("All") + flat.distinct().sorted()
        }

    suspend fun addIdea(title: String, tags: List<String>) {
        val entity = IdeaEntity(
            title = title,
            tagsCsv = tags.joinToString(", "),
            status = "Draft",
            plannedDate = null,
            createdAt = System.currentTimeMillis()
        )
        ideaDao.insert(entity)
    }

    suspend fun addTemplate(id: Int) {
        val t = when (id) {
            1 -> "TikTok quick tip" to listOf("tiktok", "tip")
            2 -> "YouTube montage" to listOf("youtube", "edit")
            else -> "Behind the scenes" to listOf("bts")
        }
        addIdea(t.first, t.second)
    }

    fun shots(ideaId: Int): Flow<List<ShotEntity>> = shotDao.forIdea(ideaId)

    suspend fun addShot(ideaId: Int, text: String) {
        val ord = shotDao.nextOrder(ideaId)
        shotDao.insert(ShotEntity(ideaId = ideaId, text = text, ord = ord))
    }

    suspend fun toggleShot(id: Int) = shotDao.toggleDone(id)
    suspend fun updateShot(id: Int, text: String) = shotDao.updateText(id, text)
    suspend fun deleteShot(id: Int) = shotDao.deleteById(id)

    suspend fun moveShotUp(id: Int) {
        db.withTransaction {
            val cur = shotDao.byId(id) ?: return@withTransaction
            val prev = shotDao.prevOf(cur.ideaId, cur.ord) ?: return@withTransaction
            shotDao.updateOrd(cur.id, prev.ord)
            shotDao.updateOrd(prev.id, cur.ord)
        }
    }

    suspend fun moveShotDown(id: Int) {
        db.withTransaction {
            val cur = shotDao.byId(id) ?: return@withTransaction
            val nxt = shotDao.nextOf(cur.ideaId, cur.ord) ?: return@withTransaction
            shotDao.updateOrd(cur.id, nxt.ord)
            shotDao.updateOrd(nxt.id, cur.ord)
        }
    }

    suspend fun setStatus(id: Int, status: String) = ideaDao.setStatus(id, status)
    suspend fun deleteIdea(id: Int) = ideaDao.deleteById(id)
    suspend fun saveIdea(id: Int, title: String, tagsCsv: String) = ideaDao.updateTitleAndTags(id, title, tagsCsv)
    suspend fun setPlannedDate(id: Int, date: Long?) = ideaDao.setPlannedDate(id, date)
    fun plannedCount(date: Long): Flow<Int> = ideaDao.countPlanned(date)
}
