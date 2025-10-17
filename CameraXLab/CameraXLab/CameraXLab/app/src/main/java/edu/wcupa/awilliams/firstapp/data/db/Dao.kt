package edu.wcupa.awilliams.firstapp.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeaDao {
    @Query(
        """
        SELECT * FROM ideas
        WHERE (:tag = 'All' OR tagsCsv LIKE '%' || :tag || '%')
          AND (:q = '' OR title LIKE '%' || :q || '%')
        ORDER BY 
            CASE WHEN :sort = 'status' THEN status END ASC,
            CASE WHEN :sort = 'title'  THEN title  END ASC,
            CASE WHEN :sort = 'date'   THEN createdAt END DESC,
            createdAt DESC
        """
    )
    fun paging(q: String, tag: String, sort: String): PagingSource<Int, IdeaEntity>

    @Insert
    suspend fun insert(entity: IdeaEntity): Long

    @Query("UPDATE ideas SET status = :status WHERE id = :id")
    suspend fun setStatus(id: Int, status: String)

    @Query("UPDATE ideas SET title = :title, tagsCsv = :tagsCsv WHERE id = :id")
    suspend fun updateTitleAndTags(id: Int, title: String, tagsCsv: String)

    @Query("DELETE FROM ideas WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE ideas SET plannedDate = :date WHERE id = :id")
    suspend fun setPlannedDate(id: Int, date: Long?)

    @Query("SELECT COUNT(*) FROM ideas WHERE plannedDate = :date")
    fun countPlanned(date: Long): Flow<Int>

    @Query("SELECT tagsCsv FROM ideas")
    fun allTagsCsv(): Flow<List<String>>
}

@Dao
interface ShotDao {
    @Query("SELECT * FROM shots WHERE ideaId = :ideaId ORDER BY ord ASC")
    fun forIdea(ideaId: Int): Flow<List<ShotEntity>>

    @Insert
    suspend fun insert(entity: ShotEntity): Long

    @Query("UPDATE shots SET done = NOT done WHERE id = :id")
    suspend fun toggleDone(id: Int)

    @Query("UPDATE shots SET text = :text WHERE id = :id")
    suspend fun updateText(id: Int, text: String)

    @Query("DELETE FROM shots WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COALESCE(MAX(ord) + 1, 0) FROM shots WHERE ideaId = :ideaId")
    suspend fun nextOrder(ideaId: Int): Int

    @Query("SELECT * FROM shots WHERE id = :id LIMIT 1")
    suspend fun byId(id: Int): ShotEntity?

    @Query("SELECT * FROM shots WHERE ideaId = :ideaId AND ord < :ord ORDER BY ord DESC LIMIT 1")
    suspend fun prevOf(ideaId: Int, ord: Int): ShotEntity?

    @Query("SELECT * FROM shots WHERE ideaId = :ideaId AND ord > :ord ORDER BY ord ASC LIMIT 1")
    suspend fun nextOf(ideaId: Int, ord: Int): ShotEntity?

    @Query("UPDATE shots SET ord = :ord WHERE id = :id")
    suspend fun updateOrd(id: Int, ord: Int)
}
