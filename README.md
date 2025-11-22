# TMDB Listing Android

Kotlin + Jetpack Compose app using Groovy DSL that surfaces TMDB trending, discover (movies/TV), search, detail, and favorites flows.

## Setup
1. Add your TMDB API key to `local.properties`:
   ```
   TMDB_API_KEY=your_key_here
   ```
2. Optional: adjust media/report API base URLs in `app/build.gradle` if needed.
3. Open the project in Android Studio (Giraffe+ recommended) and run the `app` configuration.

## Features
- Home tab with header search and trending list with infinite scroll.
- Separate Movies and TV tabs powered by TMDB discover endpoints with paging.
- Movie and TV detail pages showing backdrop/poster, title, rating, year, watch and report actions.
- TV details include seasons list with navigation to episode listings.
- Watch action hooks into a media API for playback bootstrap; report dialog posts report data to a backend endpoint.
- Favorites screen with movie/TV tabs backed by Room persistence.
