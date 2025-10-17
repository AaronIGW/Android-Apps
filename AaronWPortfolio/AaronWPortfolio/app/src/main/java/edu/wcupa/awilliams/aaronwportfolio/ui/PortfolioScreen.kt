@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package edu.wcupa.awilliams.aaronwportfolio.ui

import android.app.ActivityOptions
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import edu.wcupa.awilliams.firstapp.MainActivity as FirstAppActivity
import edu.wcupa.awilliams.acronymhero.MainActivity as AcronymHeroActivity
import edu.wcupa.awilliams.booktracker.MainActivity as BookTrackerActivity
import edu.wcupa.awilliams.carcare.MainActivity as CarCareActivity

@Composable
fun PortfolioScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Aaron's Portfolio App") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProjectButton("First App") {
                    val intent = Intent(context, FirstAppActivity::class.java)
                    context.startActivity(intent, ActivityOptions.makeCustomAnimation(
                        context,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    ).toBundle())
                }
                ProjectButton("Acronym Hero") {
                    val intent = Intent(context, AcronymHeroActivity::class.java)
                    context.startActivity(intent, ActivityOptions.makeCustomAnimation(
                        context,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    ).toBundle())
                }
                ProjectButton("Book Tracker") {
                    val intent = Intent(context, BookTrackerActivity::class.java)
                    context.startActivity(intent, ActivityOptions.makeCustomAnimation(
                        context,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    ).toBundle())
                }
                ProjectButton("Car Care") {
                    val intent = Intent(context, CarCareActivity::class.java)
                    context.startActivity(intent, ActivityOptions.makeCustomAnimation(
                        context,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    ).toBundle())
                }
            }
        }
    }
}

@Composable
private fun ProjectButton(title: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimary)
    }
}
