# Play Store release assets

Everything needed to publish **Solo Leveling Launcher** to Google Play.

## Files here
| File | Use in Play Console |
|---|---|
| `play_store_icon_512.png` | **App icon** — 512×512, 32-bit PNG (full square; Google applies the rounded mask) |
| `feature_graphic_1024x500.png` | **Feature graphic** — 1024×500 |
| `PRIVACY_POLICY.md` | **Privacy policy** — must be hosted at a public URL (see below) |
| `LISTING.md` | Draft store-listing text (title, short & full description) |

## Still needed (you must provide)
- **Phone screenshots:** 2–8 images, min 320 px, 16:9 or 9:16. Capture the home
  screen, app drawer, focus-limit sheet, and the block overlay.
- **A public URL for the privacy policy.** Easiest free option: create a public
  GitHub Gist or a GitHub Pages page with the contents of `PRIVACY_POLICY.md`,
  then paste that URL into Play Console → Policy → App content.

## One-time setup: create your upload key
Run from the project root and **remember the passwords**:

```bash
keytool -genkeypair -v \
  -keystore upload-keystore.jks \
  -alias upload \
  -keyalg RSA -keysize 2048 -validity 10000
```

Then copy `keystore.properties.example` → `keystore.properties` and fill in the
`storePassword` / `keyPassword` you chose. Both `upload-keystore.jks` and
`keystore.properties` are git-ignored — **back them up somewhere safe and never
commit them.** Losing the upload key means you can reset it via Play App
Signing, but it's far easier to just keep a backup.

## Build the release bundle
```bash
./gradlew :app:bundleRelease
```
Output: `app/build/outputs/bundle/release/app-release.aab` — this is what you
upload to Play Console.

## Publishing checklist
1. Create a Google Play developer account ($25 one-time) at https://play.google.com/console
2. Create the app → choose **Play App Signing** (recommended; keep your upload key as the upload key).
3. Upload the `.aab` to the **Internal testing** track first to validate it installs.
4. Complete: Store listing, Content rating questionnaire, Data safety form,
   Target audience, and the **Permissions declaration** (Accessibility +
   `QUERY_ALL_PACKAGES`).
5. Promote to Production and submit for review.
