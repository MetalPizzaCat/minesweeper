package com.goblynn.minesweeper

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun Tutorial(modifier: Modifier = Modifier) {
    val mines1 = remember {
        mutableStateMapOf(*(0..<3).map { y ->
            (0..<3).map { x ->
                Pair(
                    Vec2(x, y), Cell(
                        isBomb = false,
                        isFlagged = false,
                        isRevealed = x == 1 && y == 1,
                        bombCount = if (x == 1 && y == 1) {
                            4
                        } else {
                            2
                        },
                        isBorder = false
                    )
                )
            }
        }.flatten().toTypedArray())
    }
    val mines2 = remember {
        mutableStateMapOf(*(0..<3).map { y ->
            (0..<3).map { x ->
                Pair(
                    Vec2(x, y), Cell(
                        isBomb = (x == 1 && y == 2) || (x == 1 && y == 0) || (x == 0 && y == 1) || (x == 2 && y == 1),
                        isFlagged = false,
                        isRevealed = true,
                        bombCount = if (x == 1 && y == 1) {
                            4
                        } else {
                            2
                        },
                        isBorder = false
                    )
                )
            }
        }.flatten().toTypedArray())
    }

    val mines3 = remember {
        mutableStateMapOf(*(0..<3).map { y ->
            (0..<3).map { x ->
                Pair(
                    Vec2(x, y), Cell(
                        isBomb = false,
                        isFlagged = x == 1 && y == 2,
                        isRevealed = x == 1 && y == 1,
                        bombCount = if (x == 1 && y == 1) {
                            4
                        } else {
                            2
                        },
                        isBorder = false
                    )
                )
            }
        }.flatten().toTypedArray())
    }
    Column(modifier.verticalScroll(rememberScrollState())) {
        Text("How to play minesweeper", fontSize = 40.sp)
        ElevatedCard(modifier.padding(5.dp)) {
            Text(
                "Top panel",
                fontSize = 36.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Column(
                    Modifier.padding(5.dp)
                ) {
                    Text("Bomb count")
                    Text("999", fontSize = 48.sp)
                }
                Column(
                    Modifier.padding(5.dp)
                ) {
                    Text("Starts new game")
                    Button(onClick = {
                    }) {
                        Text(stringResource(R.string.restart))
                    }
                }
                Column(
                    Modifier.padding(5.dp)
                ) {
                    Text("Enable/Disable vibration")
                    FilterChip(
                        onClick = { },
                        selected = false,
                        label = { Text("Vibrate") },
                        leadingIcon =
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }

                    )
                }
            }
        }

        ElevatedCard(
            modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Playfield",
                fontSize = 36.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Column(Modifier.padding(5.dp)) {
                for (y in 0..<3) {
                    Row(Modifier.fillMaxWidth()) {
                        for (x in 0..<3) {
                            Cell(
                                mines1[Vec2(x, y)],
                                onRevealed = { },
                                onMarked = {
                                },
                                modifier = Modifier.width(48.dp)
                            )
                        }
                    }
                }
            }

            Text(
                "Click on the cell to reveal it. Number in the cell is how many cells are nearby in an 8x8 grid. In this example there 4 mines nearby",
                Modifier.padding(5.dp)
            )
            Column(Modifier.padding(5.dp)) {
                for (y in 0..<3) {
                    Row(Modifier.fillMaxWidth()) {
                        for (x in 0..<3) {
                            Cell(
                                mines2[Vec2(x, y)],
                                onRevealed = { },
                                onMarked = {
                                },
                                modifier = Modifier.width(48.dp)
                            )
                        }
                    }
                }
            }
            Text(
                "Hold down the cell to mark it as a bomb. If you mark all cells correctly you win. However you can't win if you marked a spot incorrectly",
                Modifier.padding(5.dp)
            )
            Column(Modifier.padding(5.dp)) {
                for (y in 0..<3) {
                    Row(Modifier.fillMaxWidth()) {
                        for (x in 0..<3) {
                            Cell(
                                mines3[Vec2(x, y)],
                                onRevealed = { },
                                onMarked = {
                                },
                                modifier = Modifier.width(48.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

