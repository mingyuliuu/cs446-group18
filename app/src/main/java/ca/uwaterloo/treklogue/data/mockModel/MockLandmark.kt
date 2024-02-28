package ca.uwaterloo.treklogue.data.mockModel

data class MockLandmark(
    val name: String,
    val dateVisited: String,
    val notes: String,
    val imageRes: Int // Drawable resource ID
)