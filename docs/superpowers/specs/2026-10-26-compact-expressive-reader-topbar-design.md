# Compact Expressive Reader Top Bar

**Date:** 2026-10-26  
**Status:** Implementation direction for this change

## Goal

Make Dawn's reader top bar compact and recognizably expressive, matching the floating Material 3 Expressive treatment of the updated bottom toolbar while preserving reader controls and chapter navigation.

## Approach

Use a shallow, floating pill-shaped capsule with fully rounded ends rather than a tall, full-width stacked header. Keep the status-bar inset outside the pill so it does not make the capsule itself taller. Back and bookmark actions use expressive circular buttons; the chapter title becomes the focal point; chapter/progress metadata becomes a smaller secondary line; the BAR style uses a thin animated progress indicator. Retain the existing ReaderColors palette and Material 3 icon-button shape morphing. Remove the separate DAWN / READER brand row when a chapter title is shown, but keep a compact DAWN READER fallback when the user hides the chapter title.

The alternative considered was a single-row icon toolbar. It was not chosen because long chapter titles and the existing progress-style settings would have too little room. The capsule keeps those behaviors while taking less vertical space.

## Constraints

- Keep the current vertical chapter gesture: upward advances, downward returns to the previous chapter.
- Do not change DawnChapterSwipeSurface, ReaderScreen navigation callbacks, chapter-loading logic, or the bottom toolbar.
- Preserve bookmark, back, progress style, estimate-time, and larger-touch-target behavior.
- Use existing dependencies and theme colors; do not upgrade Compose or add a UI framework.
- Keep motion small and purposeful for mid-range Android hardware.

## Verification

Run `./gradlew testDebugUnitTest` and `./gradlew assembleDebug`. Review the source diff to confirm the vertical swipe component and tests are unchanged.