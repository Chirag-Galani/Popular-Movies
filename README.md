# Popular Movies
Popular Movies
An app that displays details about popular playing movies.
Created as a part of Android Developer Nanodegree provided by Udacity.

## Udacity Project Requirements

### Common Project Requirements

- [x] App conforms to common standards found in the Android [Nanodegree General Project Guidelines](http://udacity.github.io/android-nanodegree-guidelines/core.html).

### User Interface - Layout

- [x] UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most- [x]  popular, highest rated.
- [x] Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
- [x] UI contains a screen for displaying the details for a selected movie.
- [x] Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
- [x] Movie Details layout contains a section for displaying trailer videos and user reviews.
- [x] Tablet UI uses a Master-Detail layout implemented using fragments. The left fragment is for discovering movies. The right fragment displays the movie details view for the currently selected movie.

### User Interface - Function

- [x] When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
- [x] When a movie poster thumbnail is selected, the movie details screen is launched.
- [x] When a trailer is selected, app uses an Intent to launch the trailer.
- [x] In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.

### Network API Implementation

- [x] In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.
- [x] App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.
- [x] App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

### Data Persistence

- [x] The titles and ids of the user's favorite movies are stored in a ContentProvider backed by a SQLite database. This ContentProvider is updated whenever the user favorites or unfavorites a movie.
- [x] When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the ContentProvider.
