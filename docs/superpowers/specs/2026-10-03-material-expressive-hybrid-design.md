# Dawn Material 3 Expressive Hybrid Redesign

**Date:** 2026-10-03  
**Status:** Approved design for implementation

## Goal

Turn Dawn-reader from a mostly custom-surface Material 3 app into a genuinely expressive Material 3 experience with a visual level approaching Pixel Music, while preserving the existing reading, navigation, provider, and recommendation architecture.

## Success criteria

- The active app shell has a clearly expressive Material 3 navigation treatment instead of a generic decorated surface.
- Library, Browse, For You, History, and More have distinct visual hierarchy and composition rather than identical card stacks.
- Reader controls become a compact contextual floating toolbar instead of a large persistent bottom slab.
- Material 3 Expressive motion, morphing shapes, semantic color roles, and current expressive components are used directly where they improve interaction.
- Existing chapter navigation continues to work, including swipe navigation at chapter boundaries.
- First-run recommendation discovery remains initialized after onboarding and does not repeat source browsing on normal navigation.
- The app builds successfully with the repository's existing Compose BOM and does not require an unnecessary dependency upgrade.
- The design remains usable on the target phone-sized layout and scales toward wider layouts where practical.

## Architecture

Keep the existing ViewModel, navigation, repository, provider, and reading-data architecture. Replace the visual composition layer through a small set of Dawn-specific expressive primitives plus native Material 3 Expressive components.

The main reusable layer will own visual tokens, navigation presentation, hero/shelf surfaces, and floating reader controls. Screen files will compose those primitives without moving business logic into UI components.

## Material 3 Expressive direction

The app already uses MaterialExpressiveTheme and MotionScheme.expressive(). This work makes that foundation visible in the product.

Use, where appropriate:
- MaterialExpressiveTheme
- MotionScheme.expressive()
- NavigationBar / NavigationRail
- expressive NavigationBarItem selection states
- HorizontalFloatingToolbar
- expressive FilledIconButton and IconButton shape states
- ToggleButton / segmented controls where a real toggle or mutually exclusive choice exists
- expressive button shape morphing
- spring and content-size transitions consistent with the theme motion
- semantic Material color roles instead of arbitrary one-off colors

Do not add a separate visual framework.

## App shell

Phone layout uses a floating navigation treatment with selected-item label reveal and springy state changes. The implementation should preserve the existing routes and callbacks.

The navigation surface must remain compact enough that content remains visually dominant. Larger layouts can use a Material navigation rail when the existing window-size/layout logic allows it.

## Screen treatments

### Library
Editorial opening hierarchy, a dominant continue-reading treatment, compact shelves, and grouped library content.

### Browse
Search/discovery is the primary visual action. Source discovery should feel like exploration, not settings.

### For You
Artwork-led recommendation hero plus horizontally grouped recommendation shelves.

### History
Recent reading activity and progress first, with secondary metadata de-emphasized.

### More
Grouped actions/settings with stronger section hierarchy and fewer visually identical cards.

## Reader

Replace the current persistent large reader bottom container with HorizontalFloatingToolbar or an equivalent expressive floating toolbar treatment.

Primary reader actions remain reachable:
- chapter list
- reader settings
- TTS

The toolbar should react to scrolling and collapse out of the way when the reader is actively reading. It must not interfere with chapter-edge gestures.

## Functional preservation

- Do not rewrite chapter-loading/navigation ViewModel logic unless required for the UI contract.
- Preserve existing chapter swipe behavior.
- Preserve onboarding completion semantics.
- Preserve the explicit recommendation-discovery initialization flag.
- Do not introduce repeated source discovery during ordinary recommendation/home navigation.

## Verification

Every implementation stage requires a fresh verification command. The final gate is ./gradlew assembleDebug on the resulting commit. Functional changes must have regression coverage where the behavior is represented by testable application logic; UI-only changes are additionally verified through compilation and source-level contract checks where practical.
