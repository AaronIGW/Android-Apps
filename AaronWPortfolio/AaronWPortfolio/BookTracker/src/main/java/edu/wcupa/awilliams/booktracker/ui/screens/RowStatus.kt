package edu.wcupa.awilliams.booktracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import edu.wcupa.awilliams.booktracker.R
import edu.wcupa.awilliams.booktracker.data.BookStatus

@Composable
fun RowStatus(current: BookStatus, onSet: (BookStatus) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { onSet(BookStatus.NOT_STARTED) }) { Text(LocalContext.current.getString(R.string.bt_not_started)) }
        Button(onClick = { onSet(BookStatus.READING) }) { Text(LocalContext.current.getString(R.string.bt_reading)) }
        Button(onClick = { onSet(BookStatus.FINISHED) }) { Text(LocalContext.current.getString(R.string.bt_finished)) }
    }
}
