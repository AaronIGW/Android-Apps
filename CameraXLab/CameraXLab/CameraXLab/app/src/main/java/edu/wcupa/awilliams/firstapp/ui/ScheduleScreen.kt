package edu.wcupa.awilliams.firstapp.ui

import android.widget.CalendarView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScheduleScreen(
    modifier: Modifier,
    selectedIdeaTitle: String?,
    plannedCount: Int,
    onDateChange: (Long) -> Unit,
    onAssign: (Long) -> Unit
) {
    val selected = remember { mutableLongStateOf(0L) }
    val canAssign = selectedIdeaTitle != null && selected.longValue != 0L
    Column(modifier.fillMaxSize().padding(16.dp)) {
        Text(if (selectedIdeaTitle != null) "Selected: $selectedIdeaTitle" else "Select an idea on the Ideas tab first.")
        Spacer(Modifier.height(8.dp))

        if (selectedIdeaTitle == null) {
            Text("Tip: Choose an idea in the Ideas tab, then assign it here to a calendar date.")
            Spacer(Modifier.height(8.dp))
        }

        AndroidView(factory = { ctx ->
            CalendarView(ctx).apply {
                setOnDateChangeListener { _, y, m, d ->
                    val cal = java.util.Calendar.getInstance().apply {
                        set(y, m, d, 0, 0, 0)
                        set(java.util.Calendar.MILLISECOND, 0)
                    }
                    val time = cal.timeInMillis
                    selected.longValue = time
                    onDateChange(time)
                }
            }
        })
        Spacer(Modifier.height(12.dp))
        val text = if (selected.longValue == 0L) "No date selected" else SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(selected.longValue))
        Text(text)
        Spacer(Modifier.height(8.dp))
        Text("Planned on this date: $plannedCount")
        Spacer(Modifier.height(12.dp))
        Row {
            Button(onClick = { if (selected.longValue != 0L) onAssign(selected.longValue) }, enabled = canAssign) {
                Text(if (selectedIdeaTitle == null) "Assign (select idea first)" else "Assign to $selectedIdeaTitle")
            }
        }
    }
}
