package ca.uwaterloo.treklogue.data.model

data class User(
    var id: String? = null,
    var email: String = "",
//    var journalEntries: RealmList<JournalEntry> = realmListOf()
)