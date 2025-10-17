package edu.wcupa.awilliams.firstapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.wcupa.awilliams.firstapp.data.db.ShotEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShotsScreen(
    modifier: Modifier,
    ideaId: Int,
    ideaTitle: String,
    ideaTagsCsv: String,
    shots: List<ShotEntity>,
    onToggle: (Int) -> Unit,
    onAddShot: (String) -> Unit,
    onMoveUp: (Int) -> Unit,
    onMoveDown: (Int) -> Unit,
    onDeleteShot: (Int) -> Unit,
    onUpdateShot: (Int, String) -> Unit,
    onDeleteIdea: () -> Unit,
    onSaveIdea: () -> Unit,
    onShare: () -> Unit,
    showMessage: (String) -> Unit
) {
    Column(modifier.fillMaxSize()) {
        TopAppBar(title = { Text(if (ideaTitle.isBlank()) "Shots" else ideaTitle) })
        Column(Modifier.padding(16.dp)) {
            if (ideaId == 0) {
                Text("Select an idea on the Ideas tab to start adding shots.")
                return@Column
            }

            if (shots.isEmpty()) {
                Text("Tip: Add steps here to break down your idea. Later, assign them to dates in the Schedule tab.")
                Spacer(Modifier.height(8.dp))
            }

            var shotText by remember { mutableStateOf("") }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(value = shotText, onValueChange = { shotText = it }, modifier = Modifier.weight(1f), label = { Text("Add a shot step") })
                Spacer(Modifier.width(8.dp))
                Button(onClick = { if (shotText.isNotBlank()) { onAddShot(shotText.trim()); shotText = "" } }) { Text("Add") }
            }
            Spacer(Modifier.height(12.dp))
            LazyColumn {
                items(shots, key = { it.id }) { s ->
                    ShotRow(
                        shot = s,
                        onToggle = onToggle,
                        onMoveUp = onMoveUp,
                        onMoveDown = onMoveDown,
                        onDelete = onDeleteShot,
                        onUpdate = onUpdateShot
                    )
                }
            }
        }
    }
}

@Composable
private fun ShotRow(
    shot: ShotEntity,
    onToggle: (Int) -> Unit,
    onMoveUp: (Int) -> Unit,
    onMoveDown: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    onUpdate: (Int, String) -> Unit
) {
    var text by remember(shot.id) { mutableStateOf(shot.text) }
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(Modifier.padding(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Checkbox(checked = shot.done, onCheckedChange = { onToggle(shot.id) })
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Shot") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onMoveUp(shot.id) }) { Text("↑") }
                IconButton(onClick = { onMoveDown(shot.id) }) { Text("↓") }
                IconButton(onClick = { onDelete(shot.id) }) { Text("✕") }
                IconButton(onClick = { onUpdate(shot.id, text.trim()) }) { Text("✓") }
            }
        }
    }
}
