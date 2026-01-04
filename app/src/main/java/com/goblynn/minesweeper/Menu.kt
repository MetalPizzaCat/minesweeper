package com.goblynn.minesweeper

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            LocalContext.current.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }


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
    AnimatedVisibility(view.alertAboutLastMineFailure) {
        AlertDialog(
            icon = {
                Icon(Icons.Default.Close, contentDescription = "Alert icon")
            },
            title = {
                Text("Ooops!")
            },
            text = {
                Text("You almost won but you clicked on a mine, are you sure you want to continue? There won't be a second chance!")
            },
            onDismissRequest = {
                view.looseGame()
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                       view.acceptLoss()
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                500,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                       view.rejectLoss()
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Preview
@Composable
fun MenuPreview() {
    MainMenu()
}