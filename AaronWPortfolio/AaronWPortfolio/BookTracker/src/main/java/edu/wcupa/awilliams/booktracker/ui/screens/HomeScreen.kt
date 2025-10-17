package edu.wcupa.awilliams.booktracker.ui.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.wcupa.awilliams.booktracker.R
import edu.wcupa.awilliams.booktracker.data.Book
import edu.wcupa.awilliams.booktracker.data.BookStatus
import edu.wcupa.awilliams.booktracker.ui.HomeViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val app = LocalContext.current.applicationContext as Application
    val vm: HomeViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(app))
    val books by vm.books.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = "",
            onValueChange = { vm.setSearch(it) },
            placeholder = { Text(text = LocalContext.current.getString(R.string.bt_search_hint)) },
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.setStatus(null) }) { Text(text = LocalContext.current.getString(R.string.bt_status)) }
            Button(onClick = { vm.setStatus(BookStatus.NOT_STARTED) }) { Text(text = LocalContext.current.getString(R.string.bt_not_started)) }
            Button(onClick = { vm.setStatus(BookStatus.READING) }) { Text(text = LocalContext.current.getString(R.string.bt_reading)) }
            Button(onClick = { vm.setStatus(BookStatus.FINISHED) }) { Text(text = LocalContext.current.getString(R.string.bt_finished)) }
        }
        Button(onClick = { navController.navigate("entry") }) { Text(text = LocalContext.current.getString(R.string.bt_add_book)) }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(books) { book ->
                BookRow(book) { navController.navigate("details/${book.id}") }
            }
        }
    }
}

@Composable
fun BookRow(book: Book, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = book.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = book.author, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = "${book.genre} • ${book.status} • ${book.progress}%")
            }
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}
