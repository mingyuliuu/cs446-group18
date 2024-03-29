package ca.uwaterloo.treklogue.data.model

data class JournalEntry(
    var id: String? = null,
    var landmarkId: String? = null,
    var name: String = "",
    var visitedAt: String = "",
    var photos: MutableList<String> = mutableListOf(),
    var description: String = ""
)