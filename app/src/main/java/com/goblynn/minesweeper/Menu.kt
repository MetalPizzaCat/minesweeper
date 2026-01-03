package com.goblynn.minesweeper

import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainMenu(modifier: Modifier = Modifier, view: GameView = viewModel()) {
    val vibrator =
        (LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator

    AnimatedContent(view.isPlaying) { playing ->
        if (!playing) {
            Box(modifier) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    GameMode.entries.forEach {
                        Button(
                            onClick = {
                                view.startGame(it.width, it.height, it.bombCount)
                            },
                        ) {
                            Text(stringResource(it.nameStringId))
                        }
                    }
                }
            }
        } else {
            Column(modifier) {
                Card(Modifier.padding(10.dp)) {

                    Row(Modifier.fillMaxWidth()) {
                        Text(view.remainingBombCount.toString().padStart(3, '0'), fontSize = 48.sp)
                        Button(onClick = {
                            view.startGame(
                                view.width,
                                view.height,
                                view.bombCount
                            )
                        }) {
                            Text(stringResource(R.string.restart))
                        }
                        FilterChip(
                            onClick = { view.shouldVibrate = !view.shouldVibrate },
                            selected = view.shouldVibrate,
                            label = { Text("Vibrate") },
                            leadingIcon =
                                if (view.shouldVibrate) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null

                                },

                        )
                        AnimatedVisibility(view.won) {
                            Text(stringResource(R.string.you_won))
                        }
                        AnimatedVisibility(view.lost) {
                            Text(stringResource(R.string.you_lost))
                        }
                    }
                }

                BackHandler() {
                    view.cancelGame()
                }
                Playfield(
                    view.width,
                    view.height,
                    view.cells,
                    shouldVibrate = view.shouldVibrate,
                    onCellRevealed = {
                        if (view.clickCell(it) && view.shouldVibrate) {
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    500,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                        }
                    },
                    onCellMarked = { view.markCell(it) },
                    modifier = modifier
                        .fillMaxSize()
                )
            }

        }
    }
}

@Preview
@Composable
fun MenuPreview() {
    MainMenu()
}