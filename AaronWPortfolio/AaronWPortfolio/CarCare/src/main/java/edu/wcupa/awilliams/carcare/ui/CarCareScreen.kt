package edu.wcupa.awilliams.carcare.ui

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import edu.wcupa.awilliams.carcare.R
import edu.wcupa.awilliams.carcare.data.CarTask
import edu.wcupa.awilliams.carcare.data.DefaultTasks
import edu.wcupa.awilliams.carcare.work.Scheduler

@Composable
fun CarCareScreen(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val app = ctx.applicationContext as Application
    val carName = remember { mutableStateOf("") }
    val tasks = remember { mutableStateOf(DefaultTasks.list) }
    val customName = remember { mutableStateOf("") }
    val snack = remember { SnackbarHostState() }

    Column(modifier = modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = ctx.getString(R.string.cc_hint))
        OutlinedTextField(
            value = carName.value,
            onValueChange = { carName.value = it },
            label = { Text(ctx.getString(R.string.cc_car_name_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = ctx.getString(R.string.cc_default_tasks))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tasks.value) { t ->
                        TaskRow(app, carName, t)
                    }
                }
            }
        }
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = ctx.getString(R.string.cc_add_custom))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = customName.value,
                        onValueChange = { customName.value = it },
                        label = { Text(ctx.getString(R.string.cc_task_name)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (customName.value.isNotBlank()) {
                                tasks.value = tasks.value + CarTask(customName.value.trim(), "")
                                customName.value = ""
                            }
                        }
                    ) { Text(text = ctx.getString(R.string.cc_add_custom)) }
                }
            }
        }
        SnackbarHost(hostState = snack)
    }
}

@Composable
private fun TaskRow(app: Application, carName: MutableState<String>, task: CarTask) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = task.title)
        if (task.note.isNotBlank()) Text(text = task.note)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = { if (carName.value.isNotBlank()) Scheduler.scheduleMinutes(app, carName.value, task.title, 15) },
                label = { Text(text = app.getString(R.string.cc_15m)) }
            )
            AssistChip(
                onClick = { if (carName.value.isNotBlank()) Scheduler.scheduleDays(app, carName.value, task.title, 1) },
                label = { Text(text = app.getString(R.string.cc_1d)) }
            )
            AssistChip(
                onClick = { if (carName.value.isNotBlank()) Scheduler.scheduleDays(app, carName.value, task.title, 7) },
                label = { Text(text = app.getString(R.string.cc_1w)) }
            )
            AssistChip(
                onClick = { if (carName.value.isNotBlank()) Scheduler.scheduleDays(app, carName.value, task.title, 30) },
                label = { Text(text = app.getString(R.string.cc_1mo)) }
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}
