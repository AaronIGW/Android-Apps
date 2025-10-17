package edu.wcupa.awilliams.acronymhero.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.wcupa.awilliams.acronymhero.R

@Composable
fun GameScreen(modifier: Modifier = Modifier, vm: GameViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    if (state.isGameOver) {
        Column(
            modifier = modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = stringResource(id = R.string.ah_game_over))
            Text(text = "${stringResource(id = R.string.ah_score)}: ${state.score}")
            Button(onClick = { vm.resetGame() }) { Text(text = stringResource(id = R.string.ah_play_again)) }
        }
        return
    }

    val guess = remember(state.currentAcronym) { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(id = R.string.ah_instruction))
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "${stringResource(id = R.string.ah_acronym)}:")
                Text(text = state.currentAcronym)
            }
        }

        state.categoryHint?.let { Text(text = stringResource(id = R.string.ah_category_label, it)) }
        state.firstLettersHint?.let { Text(text = stringResource(id = R.string.ah_initials_label, it), textAlign = TextAlign.Start) }

        OutlinedTextField(
            value = guess.value,
            onValueChange = { guess.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.ah_enter_expansion)) },
            isError = state.isGuessWrong,
            supportingText = {
                if (state.isGuessWrong) Text(text = stringResource(id = R.string.ah_wrong_try_again))
            },
            singleLine = true
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { vm.submitGuess(guess.value) },
                enabled = guess.value.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) { Text(text = stringResource(id = R.string.ah_submit)) }
            Button(
                onClick = { vm.skip() },
                modifier = Modifier.weight(1f)
            ) { Text(text = stringResource(id = R.string.ah_skip)) }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AssistChip(onClick = { vm.revealCategory() }, label = { Text(text = stringResource(id = R.string.ah_hint_category)) })
            AssistChip(onClick = { vm.revealFirstLetters() }, label = { Text(text = stringResource(id = R.string.ah_hint_first)) })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(text = "${stringResource(id = R.string.ah_score)}: ${state.score}")
            Text(text = "${stringResource(id = R.string.ah_round)}: ${state.wordCount}/${state.totalRounds}")
        }
    }
}
