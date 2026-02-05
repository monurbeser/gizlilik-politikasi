# Notaday

Notaday is a minimal, elegant Android daily note-taking and to-do management app built with Kotlin, Jetpack Compose, Room, Hilt, Coroutines/Flow, and WorkManager.

## Implemented Modules
- Clean architecture layers: data/domain/presentation/di
- Room entities and DAOs for notes + attachments
- Calendar/day-focused home screen
- Note detail editing with note↔todo conversion
- Attachment hooks (image/document picker with max count + size checks)
- WorkManager reminder notifications
- PDF export utility for A4 multipage documents
- Share utility for text-based app sharing
- Searchable all-notes list
- Settings screen scaffold (theme + export entry)
- Unit test for share formatting

## Build
Open in Android Studio Hedgehog+ and sync Gradle.

- Min SDK: 26
- Target SDK: 34

## Notes
- This MVP stores data locally only.
- Notification click opens app with note id payload.
- Date/time pickers are represented by quick-action shortcuts and are easy to replace with full pickers.
