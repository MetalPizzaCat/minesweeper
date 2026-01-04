package com.goblynn.minesweeper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// suppressing because current calculation uses dp
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Playfield(
    width: Int,
    height: Int,
    cells: Map<Vec2, Cell>,
    shouldVibrate: Boolean,
    onCellRevealed: (position: Vec2) -> Unit,
    onCellMarked: (position: Vec2) -> Unit,
    modifier: Modifier = Modifier
) {

    val cellWidth = LocalConfiguration.current.screenWidthDp / width

    val vibrator =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            LocalContext.current.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        for (y in 0..<height) {
            Row(Modifier.fillMaxWidth()) {
                for (x in 0..<width) {
                    Cell(
                        cells[Vec2(x, y)],
                        onRevealed = { onCellRevealed(Vec2(x, y)) },
                        onMarked = {
                            onCellMarked(Vec2(x, y))
                            if (shouldVibrate) {
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(
                                        100,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                            }
                        },
                        modifier = Modifier.width(cellWidth.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Cell(
    cell: Cell?,
    onRevealed: () -> Unit,
    onMarked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(
            if (cell == null) {
                R.drawable.bad
            } else if (cell.isRevealed) {
                if (cell.isBomb) {
                    R.drawable.bomb
                } else {
                    when (cell.bombCount) {
                        1 -> R.drawable.field_1
                        2 -> R.drawable.field_2
                        3 -> R.drawable.field_3
                        4 -> R.drawable.field_4
                        5 -> R.drawable.field_5
                        6 -> R.drawable.field_6
                        7 -> R.drawable.field_7
                        8 -> R.drawable.field_8
                        else -> R.drawable.field_base
                    }
                }
            } else if (cell.isFlagged) {
                R.drawable.flag
            } else {
                R.drawable.field_hidden
            }
        ),
        contentScale = ContentScale.FillWidth,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                enabled = true,
                onClick = { onRevealed() },
                onLongClick = { onMarked() })
    )

}

@Preview
@Composable
fun FieldPreview() {
    val width = 5
    val height = 10
    val cells = remember { mutableStateMapOf<Vec2, Cell>() }
    for (y in 0..<height) {
        for (x in 0..<width) {
            cells[Vec2(x, y)] = Cell(
                isBomb = false,
                isFlagged = false,
                isRevealed = false,
                bombCount = 0,
                isBorder = false
            )
        }

    }
    Playfield(
        width,
        height,
        cells,
        shouldVibrate = false,
        onCellMarked = {
            cells[it] = cells[it]!!.copy(isFlagged = true)
        },
        onCellRevealed = { cells[it] = cells[it]!!.copy(isRevealed = true) },
    )
}