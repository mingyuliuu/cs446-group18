package ca.uwaterloo.treklogue.data.mockModel

import android.net.Uri

data class MockJournalEntry(
    var index: Int,
    val name: String,
    val dateVisited: String,
    var description: String = "", // Optional field
    val images: MutableList<Int> = mutableListOf(), // Drawable resource IDs
    var uploadedImages: List<Uri?> = mutableListOf() // Drawable URIs
)