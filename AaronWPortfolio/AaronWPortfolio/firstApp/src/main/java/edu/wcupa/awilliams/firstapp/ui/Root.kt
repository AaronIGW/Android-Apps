package edu.wcupa.awilliams.firstapp.ui

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.wcupa.awilliams.firstapp.ui.theme.FirstAppTheme
import edu.wcupa.awilliams.firstapp.data.db.ShotEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorForgeApp() {
    FirstAppTheme {
        val vm: AppViewModel = viewModel(factory = AppViewModel.factory(LocalContext.current))
        var tab by remember { mutableIntStateOf(0) }
        var selectedIdeaId by remember { mutableIntStateOf(0) }
        var selectedIdeaTitle by remember { mutableStateOf<String?>(null) }
        var selectedDate by remember { mutableLongStateOf(0L) }
        val tags = vm.tags.collectAsState(initial = emptyList())
        val search = remember { mutableStateOf("") }
        val sortMode = vm.sortMode.collectAsState(initial = "date")
        val plannedCountState =
            if (selectedDate == 0L) null else vm.plannedCount(selectedDate).collectAsState(initial = 0)
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val ctx = LocalContext.current
        val showMessage: (String) -> Unit = { m -> scope.launch { snackbarHostState.showSnackbar(m) } }
        Scaffold(
            topBar = { TopAppBar(title = { Text("Creator Forge") }) },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = tab == 0,
                        onClick = { tab = 0 },
                        icon = { Icon(Icons.Filled.List, null) },
                        label = { Text("Ideas") }
                    )
                    NavigationBarItem(
                        selected = tab == 1,
                        onClick = { tab = 1 },
                        icon = { Icon(Icons.Filled.VideoLibrary, null) },
                        label = { Text("Shots") }
                    )
                    NavigationBarItem(
                        selected = tab == 2,
                        onClick = { tab = 2 },
                        icon = { Icon(Icons.Filled.CalendarMonth, null) },
                        label = { Text("Schedule") }
                    )
                }
            }
        ) { inner ->
            when (tab) {
                0 -> IdeasScreen(
                    modifier = Modifier.padding(inner),
                    tags = tags.value,
                    searchText = search.value,
                    sort = sortMode.value,
                    onSearchChanged = { search.value = it; vm.setSearch(it) },
                    onTagChanged = { vm.setActiveTag(it) },
                    onSortChanged = { vm.setSort(it) },
                    ideasPaged = vm.ideasPaged,
                    onAdd = { t, ts -> vm.addIdea(t, ts) },
                    onAddTemplate = { id -> vm.addTemplate(id) },
                    onSelect = { id, title ->
                        selectedIdeaId = id
                        selectedIdeaTitle = title
                        selectedDate = 0L
                        tab = 1
                    },
                    onStatusChange = { id, s -> vm.setStatus(id, s) },
                    showMessage = showMessage
                )
                1 -> {
                    val shots: List<ShotEntity> =
                        if (selectedIdeaId == 0) emptyList()
                        else vm.shots(selectedIdeaId).collectAsState(initial = emptyList()).value
                    val title = selectedIdeaTitle ?: ""
                    val tagsCsv = ""
                    ShotsScreen(
                        modifier = Modifier.padding(inner),
                        ideaId = selectedIdeaId,
                        ideaTitle = title,
                        ideaTagsCsv = tagsCsv,
                        shots = shots,
                        onToggle = { id -> vm.toggleShot(id) },
                        onAddShot = { text -> if (selectedIdeaId != 0) vm.addShot(selectedIdeaId, text) },
                        onMoveUp = { id -> vm.moveShotUp(id) },
                        onMoveDown = { id -> vm.moveShotDown(id) },
                        onDeleteShot = { id -> vm.deleteShot(id) },
                        onUpdateShot = { id, text -> vm.updateShot(id, text) },
                        onDeleteIdea = {
                            if (selectedIdeaId != 0) {
                                vm.deleteIdea(selectedIdeaId)
                                selectedIdeaId = 0
                                selectedIdeaTitle = null
                                tab = 0
                                showMessage("Idea deleted")
                            }
                        },
                        onSaveIdea = { showMessage("Saved") },
                        onShare = {
                            val body = buildString {
                                append("Creator Forge idea\n")
                                append("Title: ").append(title).append('\n')
                                if (tagsCsv.isNotBlank()) append("Tags: ").append(tagsCsv).append('\n')
                                if (shots.isNotEmpty()) {
                                    append("Shots:\n")
                                    shots.forEachIndexed { i, s ->
                                        append("${i + 1}. ${s.text}${if (s.done) " ✓" else ""}\n")
                                    }
                                }
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, body)
                            }
                            ctx.startActivity(Intent.createChooser(intent, "Share"))
                        },
                        showMessage = showMessage
                    )
                }
                else -> {
                    val title = selectedIdeaTitle
                    ScheduleScreen(
                        modifier = Modifier.padding(inner),
                        selectedIdeaTitle = title,
                        plannedCount = plannedCountState?.value ?: 0,
                        onDateChange = { selectedDate = it },
                        onAssign = { date -> if (selectedIdeaId != 0) vm.setPlannedDate(selectedIdeaId, date) }
                    )
                }
            }
        }
    }
}
