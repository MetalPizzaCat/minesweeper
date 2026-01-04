package com.goblynn.minesweeper

enum class GameMode(val nameStringId: Int, val width: Int, val height: Int, val bombCount: Int) {
    TEST(R.string.gamemode_test_name, 5, 8, 1),
    GAMBLING(R.string.gamemode_gambling_name, 3, 3, 8),
    EASY(R.string.gamemode_easy_name, 5, 7, 9),
    MEDIUM(R.string.gamemode_medium_name, 10, 14, 30),
    HARD(R.string.gamemode_hard_name, 15, 21, 40),
    ABSURD(R.string.gamemode_absurd_name, 20, 28, 50)
}