package com.emptycastle.novery.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class DawnChapterSwipeGestureTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun upwardSwipeNavigatesToNextChapterWhileContentMovesWithFinger() {
        var nextCount by mutableIntStateOf(0)

        composeRule.setContent {
            MaterialTheme {
                DawnChapterSwipeSurface(
                    chapterKey = "chapter-1",
                    enabled = true,
                    hasPreviousChapter = true,
                    hasNextChapter = true,
                    reduceMotion = true,
                    onPrevious = {},
                    onNext = { nextCount++ },
                    onTap = { _, _, _ -> },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(Modifier.fillMaxSize())
                }
            }
        }

        composeRule.onRoot().performTouchInput {
            swipeUp(startY = 0.85f, endY = 0.15f, durationMillis = 350)
        }

        composeRule.waitUntil(3_000) { nextCount == 1 }
        composeRule.runOnIdle { assertEquals(1, nextCount) }
    }

    @Test
    fun downwardSwipeNavigatesToPreviousChapterWhileContentMovesWithFinger() {
        var previousCount by mutableIntStateOf(0)

        composeRule.setContent {
            MaterialTheme {
                DawnChapterSwipeSurface(
                    chapterKey = "chapter-2",
                    enabled = true,
                    hasPreviousChapter = true,
                    hasNextChapter = true,
                    reduceMotion = true,
                    onPrevious = { previousCount++ },
                    onNext = {},
                    onTap = { _, _, _ -> },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(Modifier.fillMaxSize())
                }
            }
        }

        composeRule.onRoot().performTouchInput {
            swipeDown(startY = 0.15f, endY = 0.85f, durationMillis = 350)
        }

        composeRule.waitUntil(3_000) { previousCount == 1 }
        composeRule.runOnIdle { assertEquals(1, previousCount) }
    }
}
