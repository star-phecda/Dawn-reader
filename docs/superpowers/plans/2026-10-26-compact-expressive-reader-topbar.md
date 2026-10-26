# Compact Expressive Reader Top Bar Implementation Plan

> **For agentic workers:** Follow this plan task-by-task and verify each result before calling the work complete.

**Goal:** Reduce the reader header's vertical footprint and make it feel like Dawn's expressive floating bottom toolbar.

**Architecture:** Change only the reader header composable and its progress metadata styling. Keep reader navigation, gesture handling, ViewModels, and bottom controls unchanged.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, existing Dawn Reader theme.

**Spec:** `docs/superpowers/specs/2026-10-26-compact-expressive-reader-topbar-design.md`

## Global Constraints

- Keep vertical swipe navigation intact: upward = next chapter; downward = previous chapter.
- Preserve the existing ReaderTopBar signature and callback semantics.
- Preserve progress styles, bookmark state, title-visibility setting, and larger touch targets.
- Do not add dependencies or upgrade the Compose BOM.
- Keep animations subtle and avoid large blur effects or continuous decorative animation.

## Review Focus

- Hidden chapter title: ensure the DAWN READER fallback appears without revealing the hidden title.
- Empty title and disabled progress: keep chapter context visible.
- BAR progress: retain the progress percentage/chapter metadata and show an animated thin line.
- Alternate progress styles: keep the selected style and estimated time legible in a single compact metadata row.
- Large touch targets: retain the user's accessibility preference.

---

### Task 1: Compact the reader top bar

**Files:**
- Modify: `app/src/main/java/com/emptycastle/novery/ui/screens/reader/components/ReaderOverlays.kt`

**Implementation decisions:**
- Use a compact asymmetric floating capsule with a subtle outline and elevation.
- Place back and bookmark actions at the edges, chapter title in the center, and metadata underneath.
- Reduce default icon targets to 40 dp while retaining 56 dp when larger touch targets are enabled.
- Use the existing ProgressIndicator modes with smaller label typography; animate the BAR indicator over 220 ms.
- Keep `DawnChapterSwipeSurface` and `DawnChapterSwipeTest` untouched.

**Verification:**
- Run `./gradlew testDebugUnitTest`.
- Run `./gradlew assembleDebug`.
- Inspect the diff to confirm only the header/progress presentation and documentation changed; no gesture/navigation code changed.