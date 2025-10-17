package edu.wcupa.awilliams.booktracker.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.wcupa.awilliams.booktracker.R
import edu.wcupa.awilliams.booktracker.data.BookStatus
import edu.wcupa.awilliams.booktracker.ui.BookEntryViewModel

@Composable
fun BookEntryScreen(navController: NavController) {
    val app = LocalContext.current.applicationContext as Application
    val vm: BookEntryViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(app))

    val title = remember { mutableStateOf("") }
    val author = remember { mutableStateOf("") }
    val genre = remember { mutableStateOf("") }
    val status = remember { mutableStateOf(BookStatus.NOT_STARTED) }
    val progress = remember { mutableStateOf(0f) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(value = title.value, onValueChange = { title.value = it }, label = { Text(LocalContext.current.getString(R.string.bt_title)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = author.value, onValueChange = { author.value = it }, label = { Text(LocalContext.current.getString(R.string.bt_author)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = genre.value, onValueChange = { genre.value = it }, label = { Text(LocalContext.current.getString(R.string.bt_genre)) }, modifier = Modifier.fillMaxWidth())
        RowStatus(status.value) { status.value = it }
        Text(text = LocalContext.current.getString(R.string.bt_progress) + " ${progress.value.toInt()}%")
        Slider(value = progress.value, onValueChange = { progress.value = it }, valueRange = 0f..100f)
        Button(onClick = {
            vm.add(title.value, author.value, genre.value, status.value, progress.value.toInt())
            navController.popBackStack()
        }) { Text(LocalContext.current.getString(R.string.bt_save)) }
        TextButton(onClick = { navController.popBackStack() }) { Text(LocalContext.current.getString(R.string.bt_cancel)) }
        Spacer(Modifier.height(8.dp))
    }
}
