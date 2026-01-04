package com.goblynn.minesweeper

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(modifier: Modifier = Modifier) {
    var currentTab by remember { mutableStateOf(AppPages.GAME) }
    Scaffold(modifier) { padding ->
        Column(Modifier.padding(padding)) {
            PrimaryTabRow(currentTab.ordinal) {
                AppPages.entries.forEachIndexed { i, page ->
                    Tab(selected = i == currentTab.ordinal, onClick = { currentTab = page }) {
                        Text(stringResource(page.nameStringId))
                    }
                }
            }
            AnimatedContent(currentTab) {
                when (it) {
                    AppPages.GAME -> MainMenu()
                    AppPages.TUTORIAL -> Tutorial(Modifier.padding(5.dp))
                }
            }
        }
    }
}