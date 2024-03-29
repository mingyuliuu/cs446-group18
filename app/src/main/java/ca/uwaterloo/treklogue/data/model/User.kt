package ca.uwaterloo.treklogue.data.model

data class User(
    var id: String? = null,
    var email: String = "",
    var journalEntries: MutableList<JournalEntry> = mutableListOf()
)