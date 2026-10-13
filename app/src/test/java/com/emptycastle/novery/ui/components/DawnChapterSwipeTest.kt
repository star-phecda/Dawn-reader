package com.emptycastle.novery.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DawnChapterSwipeTest {
    @Test
    fun upwardSwipeSelectsNextChapter() {
        assertEquals(
            ChapterSwipeDirection.NEXT,
            chapterSwipeDirectionForDrag(
                totalDx = 2f,
                totalDy = -180f,
                touchSlopPx = 8f,
                canPrevious = true,
                canNext = true
            )
        )
    }

    @Test
    fun downwardSwipeSelectsPreviousChapter() {
        assertEquals(
            ChapterSwipeDirection.PREVIOUS,
            chapterSwipeDirectionForDrag(
                totalDx = 1f,
                totalDy = 180f,
                touchSlopPx = 8f,
                canPrevious = true,
                canNext = true
            )
        )
    }

    @Test
    fun downwardSwipeAtFirstChapterDoesNotNavigateBackward() {
        assertNull(
            chapterSwipeDirectionForDrag(
                totalDx = 1f,
                totalDy = 180f,
                touchSlopPx = 8f,
                canPrevious = false,
                canNext = true
            )
        )
    }

    @Test
    fun upwardSwipeAtLastChapterDoesNotNavigateForward() {
        assertNull(
            chapterSwipeDirectionForDrag(
                totalDx = 1f,
                totalDy = -180f,
                touchSlopPx = 8f,
                canPrevious = true,
                canNext = false
            )
        )
    }

    @Test
    fun horizontalSwipeDoesNotSelectChapter() {
        assertNull(
            chapterSwipeDirectionForDrag(
                totalDx = 180f,
                totalDy = 40f,
                touchSlopPx = 8f,
                canPrevious = true,
                canNext = true
            )
        )
    }
}
