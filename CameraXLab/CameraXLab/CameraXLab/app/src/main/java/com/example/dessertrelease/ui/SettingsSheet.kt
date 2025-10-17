package com.example.dessertrelease.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    state: DessertReleaseUiState,
    onLayoutChange: (Boolean) -> Unit,
    onSortChange: (SortOrder) -> Unit,
    onDynamicColor: (Boolean) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(20.dp)) {
            Text("Layout")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = state.isLinearLayout, onClick = { onLayoutChange(true) }, label = { Text("List") })
                FilterChip(selected = !state.isLinearLayout, onClick = { onLayoutChange(false) }, label = { Text("Grid") })
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
            Text("Sort")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = state.sortOrder == SortOrder.AZ, onClick = { onSortChange(SortOrder.AZ) }, label = { Text("A → Z") })
                FilterChip(selected = state.sortOrder == SortOrder.ZA, onClick = { onSortChange(SortOrder.ZA) }, label = { Text("Z → A") })
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Dynamic color")
                Switch(checked = state.dynamicColor, onCheckedChange = onDynamicColor)
            }
            Spacer(Modifier.height(24.dp))
            Button(onClick = onReset, modifier = Modifier.fillMaxWidth()) { Text("Reset to defaults") }
            Spacer(Modifier.height(12.dp))
        }
    }
}
