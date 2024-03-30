package ca.uwaterloo.treklogue.data.model

data class JournalEntry(
    var name: String = "",
    var visitedAt: String = "",
    var photos: MutableList<String> = mutableListOf(),
    var description: String = ""
)