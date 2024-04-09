package ca.uwaterloo.treklogue.data.model

data class JournalEntry(
    var index: Int = 0,
    var landmarkId: String = "",
    var name: String = "",
    var visitedAt: String = "",
    var photos: MutableList<String> = mutableListOf(),
    var description: String = "",
    var rating: Float = 0f,
)