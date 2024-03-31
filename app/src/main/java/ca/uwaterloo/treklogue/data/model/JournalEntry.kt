package ca.uwaterloo.treklogue.data.model

data class JournalEntry(
    var index: Int = 0,
    var name: String = "",
    var visitedAt: String = "",
    var photos: MutableList<String> = mutableListOf(),
    var description: String = ""
)