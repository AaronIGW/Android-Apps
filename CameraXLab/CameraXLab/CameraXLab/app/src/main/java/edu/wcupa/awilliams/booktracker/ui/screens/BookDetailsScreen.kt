package edu.wcupa.awilliams.booktracker.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.wcupa.awilliams.booktracker.R
import edu.wcupa.awilliams.booktracker.data.Book
import edu.wcupa.awilliams.booktracker.ui.BookEditViewModel

@Composable
fun BookDetailsScreen(navController: NavController, id: Int) {
    val app = LocalContext.current.applicationContext as Application
    val vm: BookEditViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(app))
    val book by vm.get(id).collectAsState(initial = null)
    if (book != null) {
        DetailsContent(navController, vm, book!!)
    }
}

@Composable
private fun DetailsContent(navController: NavController, vm: BookEditViewModel, book: Book) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = LocalContext.current.getString(R.string.bt_details))
        Text(text = book.title)
        Text(text = book.author)
        Text(text = book.genre)
        Text(text = "${book.status} ${book.progress}%")
        Button(onClick = { navController.navigate("edit/${book.id}") }) { Text(LocalContext.current.getString(R.string.bt_edit)) }
        Button(onClick = {
            vm.delete(book)
            navController.popBackStack()
        }) { Text(LocalContext.current.getString(R.string.bt_delete)) }
    }
}
