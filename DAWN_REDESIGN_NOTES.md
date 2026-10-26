# Dawn 0.4 — Unorthodox interaction direction

Dawn is moving beyond a conventional reader layout into an interaction-led visual identity inspired by the experimental design language used across the project's companion web work.

## Visual principles
- Break the grid when doing so gives the interface personality or communicates hierarchy.
- Prefer editorial composition, asymmetry, oversized typography, floating shapes and authored spacing over uniform card grids.
- Use motion to communicate structure: transitions should feel attached to objects and gestures.
- Keep reading text calm, spacious and highly legible; the surrounding chrome can be expressive.
- Treat covers, chapter titles and progress as visual material rather than mere metadata.
- Avoid continuous decorative animation and large blur effects so the style remains performant on mid-range Android hardware.

## Reader interaction

- Keep the current full-reader vertical chapter gesture as the active design.
- Swipe upward to advance to the next chapter; swipe downward to return to the previous chapter.
- The reader page follows the finger during a deliberate vertical drag.
- Crossing the chapter-change threshold gives haptic feedback; releasing commits the navigation, while a shorter drag springs back.
- Short vertical movement remains normal reading scroll; horizontal chapter swiping is deferred rather than part of the current interaction.
- The gesture is disabled while chapter-list or TTS interaction is active.
  
## Existing Dawn foundations
- Deep-night palette with cyan, electric blue, violet and magenta accents.
- Material 3 Expressive components underneath the authored layer.
- Floating capsule navigation and cover-led layouts.
- Existing Novery data/install continuity remains intact.
