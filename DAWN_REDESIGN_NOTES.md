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
- Swipe left to advance to the next chapter.
- Swipe right to return to the previous chapter.
- The current chapter physically follows the finger.
- The adjacent direction is revealed underneath while dragging.
- Crossing the threshold produces haptic feedback; releasing commits the transition.
- Releasing before the threshold springs the chapter back into place.
- Vertical movement remains ordinary reading scroll.
- The gesture is available in immersive reading mode and is disabled while reader controls, chapter sheets or TTS interaction are active.

## Existing Dawn foundations
- Deep-night palette with cyan, electric blue, violet and magenta accents.
- Material 3 Expressive components underneath the authored layer.
- Floating capsule navigation and cover-led layouts.
- Existing Novery data/install continuity remains intact.
