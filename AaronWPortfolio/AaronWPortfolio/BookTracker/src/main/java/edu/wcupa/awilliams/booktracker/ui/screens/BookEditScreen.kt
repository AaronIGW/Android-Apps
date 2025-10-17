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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.wcupa.awilliams.booktracker.R
import edu.wcupa.awilliams.booktracker.data.Book
import edu.wcupa.awilliams.booktracker.data.BookStatus
import edu.wcupa.awilliams.booktracker.ui.BookEditViewModel

@Composable
fun BookEditScreen(navController: NavController, id: Int) {
    val app = LocalContext.current.applicationContext as Application
    val vm: BookEditViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(app))
    val bookFlow = vm.get(id)
    val book by bookFlow.collectAsState(initial = null)

    if (book != null) {
        EditForm(navController, vm, book!!)
    }
}

@Composable
private fun EditForm(navController: NavController, vm: BookEditViewModel, book: Book) {
    val title = remember { mutableStateOf(book.title) }
    val author = remember { mutableStateOf(book.author) }
    val genre = remember { mutableStateOf(book.genre) }
    val status = remember { mutableStateOf(book.status) }
    val progress = remember { mutableStateOf(book.progress.toFloat()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(value = title.value, onValueChange = { title.value = it }, label = { Text(LocalContext.current.getString(R.string.bt_title)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = author.value, onValueChange = { author.value = it }, label = { Text(LocalContext.current.getString(R.string.bt_author)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = genre.value, onValueChange = { genre.value = it }, label = { Text(LocalContext.current.getString(R.string.bt_genre)) }, modifier = Modifier.fillMaxWidth())
        RowStatus(status.value) { status.value = it }
        Text(text = LocalContext.current.getString(R.string.bt_progress) + " ${progress.value.toInt()}%")
        Slider(value = progress.value, onValueChange = { progress.value = it }, valueRange = 0f..100f)
        Button(onClick = {
            vm.update(book.id, title.value, author.value, genre.value, status.value, progress.value.toInt())
            navController.popBackStack()
        }) { Text(LocalContext.current.getString(R.string.bt_update)) }
        Button(onClick = {
            vm.delete(book)
            navController.popBackStack()
        }) { Text(LocalContext.current.getString(R.string.bt_delete)) }
        TextButton(onClick = { navController.popBackStack() }) { Text(LocalContext.current.getString(R.string.bt_cancel)) }
        Spacer(Modifier.height(8.dp))
    }
}
