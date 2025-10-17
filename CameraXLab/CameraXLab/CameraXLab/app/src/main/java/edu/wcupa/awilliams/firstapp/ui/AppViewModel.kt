package edu.wcupa.awilliams.firstapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import edu.wcupa.awilliams.firstapp.data.Repository
import edu.wcupa.awilliams.firstapp.data.db.AppDatabase
import edu.wcupa.awilliams.firstapp.data.db.IdeaEntity
import edu.wcupa.awilliams.firstapp.data.db.ShotEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class AppViewModel(private val repo: Repository) : ViewModel() {
    private val search = MutableStateFlow("")
    private val activeTag = MutableStateFlow("All")
    private val sortModeState = MutableStateFlow("date")

    val sortMode: StateFlow<String> = sortModeState
    val tags: Flow<List<String>> = repo.tags()

    val ideasPaged: Flow<PagingData<IdeaEntity>> =
        combine(search, activeTag, sortModeState) { q, tag, sort -> Triple(q, tag, sort) }
            .flatMapLatest { (q, tag, sort) -> repo.ideasPaged(q, tag, sort) }
            .cachedIn(viewModelScope)

    fun setSearch(v: String) { search.value = v }
    fun setActiveTag(v: String) { activeTag.value = v }
    fun setSort(v: String) { sortModeState.value = v }

    fun addIdea(title: String, tags: List<String>) = viewModelScope.launch { repo.addIdea(title, tags) }
    fun addTemplate(id: Int) = viewModelScope.launch { repo.addTemplate(id) }
    fun setStatus(id: Int, status: String) = viewModelScope.launch { repo.setStatus(id, status) }

    fun shots(ideaId: Int): Flow<List<ShotEntity>> = repo.shots(ideaId)
    fun addShot(ideaId: Int, text: String) = viewModelScope.launch { repo.addShot(ideaId, text) }
    fun toggleShot(id: Int) = viewModelScope.launch { repo.toggleShot(id) }
    fun moveShotUp(id: Int) = viewModelScope.launch { repo.moveShotUp(id) }
    fun moveShotDown(id: Int) = viewModelScope.launch { repo.moveShotDown(id) }
    fun updateShot(id: Int, text: String) = viewModelScope.launch { repo.updateShot(id, text) }
    fun deleteShot(id: Int) = viewModelScope.launch { repo.deleteShot(id) }

    fun deleteIdea(id: Int) = viewModelScope.launch { repo.deleteIdea(id) }
    fun saveIdea(id: Int, title: String, tagsCsv: String) = viewModelScope.launch { repo.saveIdea(id, title, tagsCsv) }
    fun setPlannedDate(id: Int, date: Long?) = viewModelScope.launch { repo.setPlannedDate(id, date) }
    fun plannedCount(date: Long): Flow<Int> = repo.plannedCount(date)

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val db = AppDatabase.getInstance(context)
                    val repo = Repository(db, db.ideaDao(), db.shotDao())
                    @Suppress("UNCHECKED_CAST")
                    return AppViewModel(repo) as T
                }
            }
    }
}
