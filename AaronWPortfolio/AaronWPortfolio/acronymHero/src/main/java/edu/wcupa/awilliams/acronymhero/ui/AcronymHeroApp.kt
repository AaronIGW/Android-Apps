package edu.wcupa.awilliams.acronymhero.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import edu.wcupa.awilliams.acronymhero.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcronymHeroApp() {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) }
    ) { padding ->
        GameScreen(modifier = Modifier.padding(padding))
    }
}
