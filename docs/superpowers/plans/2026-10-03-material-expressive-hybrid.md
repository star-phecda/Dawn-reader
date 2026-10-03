# Dawn Material 3 Expressive Hybrid Redesign Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Deliver the approved hybrid Material 3 Expressive redesign of Dawn-reader while fixing the current CI failure and preserving reading/navigation behavior.

**Architecture:** Keep existing state/data/navigation architecture. Build a reusable expressive visual layer around the existing screens, use native Material 3 Expressive components for navigation and reader chrome, and make each screen consume the shared layer rather than inventing new surface styles.

**Tech Stack:** Kotlin 2.2.10, Android Gradle Plugin 9.2.0, Jetpack Compose, Material 3 Expressive, existing Compose BOM 2026.09.00.

**Spec:** docs/superpowers/specs/2026-10-03-material-expressive-hybrid-design.md

## Global Constraints

- Keep the repository's existing Compose BOM 2026.09.00 unless compilation proves a dependency is genuinely missing.
- Do not replace the existing ViewModel/data/provider architecture.
- Use Material semantic color roles instead of arbitrary per-component colors.
- Prefer native Material 3 Expressive components before custom equivalents.
- Reader controls must be compact and must not interfere with chapter-edge swipe handling.
- Recommendation discovery must stay one-time after onboarding initialization.

## Review Focus

- Small phone viewport: navigation and reader controls remain compact and do not consume excessive reading area.
- Reader scroll/gesture overlap: collapsing toolbar must not swallow chapter-edge gestures.
- Dynamic-color/custom-theme modes: expressive surfaces must continue using valid semantic roles.
- Empty/loading/error states: the redesigned shell must not assume populated recommendation/library data.
- Existing navigation routes: visual rewrites must preserve route callbacks and selected-tab state.

---

### Task 1: Recover the build baseline

**Files:** app/src/main/java/com/emptycastle/novery/ui/components/ReaderBottomBar.kt

- [ ] Confirm the known CI failure.
- [ ] Add the missing widthIn import.
- [ ] Run ./gradlew assembleDebug.
- [ ] Commit fix: restore reader bottom bar build.

### Task 2: Establish the expressive visual layer

**Files:** Theme.kt, DawnComponents.kt, new DawnExpressiveComponents.kt

- [ ] Add source-level contract coverage for the reusable expressive primitives and run it red.
- [ ] Refine theme shape/motion/color token usage while preserving dynamic/custom themes.
- [ ] Add reusable expressive hero/shelf/section primitives and compact selection states.
- [ ] Run tests and ./gradlew assembleDebug.
- [ ] Commit feat: establish Dawn expressive visual primitives.

### Task 3: Replace the home navigation shell

**Files:** BottomNavBar.kt, HomeScreen.kt

- [ ] Add a regression check for route-to-selected-item mapping and run it red if needed.
- [ ] Implement compact expressive navigation using Material NavigationBar semantics.
- [ ] Preserve routes, callbacks, and insets.
- [ ] Run verification.
- [ ] Commit feat: redesign expressive home navigation.

### Task 4: Redesign the five home tabs

**Files:** LibraryTab.kt, BrowseTab.kt, RecommendationTab.kt, HistoryTab.kt, MoreTab.kt

- [ ] Cover empty/loading paths and run checks red where new contracts are introduced.
- [ ] Implement Library editorial/continue-reading hierarchy.
- [ ] Implement Browse search/discovery hierarchy.
- [ ] Implement For You artwork-led hero and shelf rhythm.
- [ ] Implement History and More as distinct activity/action surfaces.
- [ ] Run ./gradlew assembleDebug and tests.
- [ ] Commit feat: redesign Dawn home tabs.

### Task 5: Replace the reader bottom slab

**Files:** ReaderBottomBar.kt, ReaderScreen.kt

- [ ] Add a regression check for toolbar action availability and run it red.
- [ ] Integrate HorizontalFloatingToolbar with reader scrolling.
- [ ] Prevent toolbar interaction from swallowing chapter-edge gestures.
- [ ] Preserve chapter list, settings, and TTS actions.
- [ ] Run verification and ./gradlew assembleDebug.
- [ ] Commit feat: make reader controls expressive and compact.

### Task 6: Preserve discovery and chapter navigation

**Files:** OnboardingViewModel.kt, Recommendation/discovery ViewModel if needed, DawnChapterSwipe.kt, ReaderScreen.kt

- [ ] Add regression tests for discovery initialization and chapter-boundary navigation and run them red.
- [ ] Fix only implementation gaps revealed by tests.
- [ ] Run focused tests and full build.
- [ ] Commit fix: preserve discovery and chapter navigation contracts.

### Task 7: Final verification

- [ ] Run the repository test suite.
- [ ] Run ./gradlew assembleDebug from a clean state.
- [ ] Inspect the complete diff.
- [ ] Run final review against the spec/review focus.
- [ ] Commit only concrete verification fixes.
