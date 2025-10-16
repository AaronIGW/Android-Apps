@file:OptIn(
    androidx.compose.foundation.layout.ExperimentalLayoutApi::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class
)

package edu.wcupa.awilliams.firstapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import edu.wcupa.awilliams.firstapp.data.db.IdeaEntity
import kotlinx.coroutines.flow.Flow

@Composable
fun IdeasScreen(
    modifier: Modifier,
    tags: List<String>,
    searchText: String,
    sort: String,
    onSearchChanged: (String) -> Unit,
    onTagChanged: (String) -> Unit,
    onSortChanged: (String) -> Unit,
    ideasPaged: Flow<PagingData<IdeaEntity>>,
    onAdd: (String, List<String>) -> Unit,
    onAddTemplate: (Int) -> Unit,
    onSelect: (Int, String) -> Unit,
    onStatusChange: (Int, String) -> Unit,
    showMessage: (String) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var tagsText by rememberSaveable { mutableStateOf("") }
    val ideas = ideasPaged.collectAsLazyPagingItems()

    Column(modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = searchText, onValueChange = onSearchChanged, modifier = Modifier.fillMaxWidth(), label = { Text("Search") })
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = title, onValueChange = { title = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Idea") })
        Spacer(Modifier.height(8.dp))
        Row {
            OutlinedTextField(value = tagsText, onValueChange = { tagsText = it }, modifier = Modifier.weight(1f), label = { Text("Tags (comma)") })
            TextButton(onClick = {
                val t = title.trim()
                if (t.isEmpty()) { showMessage("Enter a title"); return@TextButton }
                val ts = tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                onAdd(t, ts)
                title = ""
                tagsText = ""
            }) { Text("Add") }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(onClick = { onAddTemplate(1) }, label = { Text("Templates") })
            TextButton(onClick = { onTagChanged("All") }) { Text("All") }
        }
        Spacer(Modifier.height(8.dp))
        SingleChoiceSegmentedButtonRow {
            SegmentedButton(selected = sort == "date", onClick = { onSortChanged("date") }, shape = SegmentedButtonDefaults.itemShape(0,3)) { Text("Newest") }
            SegmentedButton(selected = sort == "status", onClick = { onSortChanged("status") }, shape = SegmentedButtonDefaults.itemShape(1,3)) { Text("Status") }
            SegmentedButton(selected = sort == "title", onClick = { onSortChanged("title") }, shape = SegmentedButtonDefaults.itemShape(2,3)) { Text("A→Z") }
        }
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            tags.forEach { t ->
                FilterChip(selected = false, onClick = { onTagChanged(t) }, label = { Text(t) })
            }
        }
        Spacer(Modifier.height(8.dp))

        Text("Tip: Tap an idea below to add detailed shots.")

        LazyColumn(contentPadding = PaddingValues(bottom = 120.dp)) {
            items(ideas.itemSnapshotList.items, key = { it.id }) { idea ->
                IdeaRow(idea = idea, onClick = { onSelect(idea.id, idea.title) }, onStatusChange = onStatusChange)
            }
        }
    }
}

@Composable
private fun IdeaRow(idea: IdeaEntity, onClick: () -> Unit, onStatusChange: (Int, String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() }) {
        Column(Modifier.padding(16.dp)) {
            Text(idea.title)
            Spacer(Modifier.height(4.dp))
            Text(idea.tagsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }.joinToString(" • "))
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = idea.status == "Draft", onClick = { onStatusChange(idea.id, "Draft") }, label = { Text("Draft") })
                FilterChip(selected = idea.status == "Ready", onClick = { onStatusChange(idea.id, "Ready") }, label = { Text("Ready") })
                FilterChip(selected = idea.status == "Shot", onClick = { onStatusChange(idea.id, "Shot") }, label = { Text("Shot") })
            }
        }
    }
}
