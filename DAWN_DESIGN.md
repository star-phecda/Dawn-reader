# Dawn — redesign notes

Dawn is the first visual redesign pass built on top of the original Novery codebase.
The goal is to preserve the novel-source/data/reader machinery while replacing the generic
app-shell feel with a warm, expressive reading product.

## First pass

- Floating rounded Material 3-style navigation capsule with animated selected destinations.
- Dawn sunrise palette: coral/peach primary, lavender secondary, gold tertiary, ink dark surfaces.
- Nunito for UI copy and Cormorant Garamond for display/headline hierarchy.
- Library header upgraded to a clear editorial hierarchy with "My Library" and a wide search field.
- Dawn mascot image used for the launcher/splash identity.
- Reader keeps its existing settings/TTS/history logic and gains a short chapter-edge horizontal swipe using the existing next/previous navigation methods.

## Next visual pass

- Full home/discovery editorial layout.
- Novel details page with cover atmosphere, tags and reading CTA.
- Library card/grid refresh.
- Search/discovery redesign.
- Reader chrome and chapter-transition visual affordance.
- Settings redesign and cohesive empty/loading/error illustrations.

## Forking notes

The Gradle `applicationId` and Kotlin package namespace are intentionally unchanged in this first pass
so an installed build can retain the original app's local data while the app label and visual identity become Dawn.
The original project copyright/license notices and upstream links are retained.
