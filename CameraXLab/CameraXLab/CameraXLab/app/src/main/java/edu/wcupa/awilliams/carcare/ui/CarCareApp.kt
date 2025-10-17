package edu.wcupa.awilliams.carcare.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.wcupa.awilliams.carcare.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarCareApp() {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.cc_title)) }) }
    ) { padding ->
        CarCareScreen(modifier = Modifier.padding(padding))
    }
}
