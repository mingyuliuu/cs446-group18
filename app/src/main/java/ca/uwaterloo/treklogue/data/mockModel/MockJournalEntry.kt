package ca.uwaterloo.treklogue.data.mockModel

data class MockJournalEntry(
    val name: String,
    val dateVisited: String,
    val description: String = "", // Optional field
    val images: MutableList<Int> = mutableListOf() // Drawable resource IDs
)