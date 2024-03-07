package ca.uwaterloo.treklogue.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockJournalEntry
import ca.uwaterloo.treklogue.data.mockModel.MockLandmark
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class JournalEntryViewModel : ViewModel() {
    private val _selectedJournalEntry = MutableLiveData<MockJournalEntry>()
    val selectedJournalEntry: LiveData<MockJournalEntry> get() = _selectedJournalEntry

    fun selectJournalEntry(journalEntry: MockJournalEntry) {
        _selectedJournalEntry.value = journalEntry
    }

    fun createJournalEntry(landmark: MockLandmark) {
        val dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val newJournalEntry = MockJournalEntry (landmark.name, dateString)
        _selectedJournalEntry.value = newJournalEntry
    }

    // Mock: List of journal entries
    val journalEntries = listOf(
        MockJournalEntry(
            "Eiffel Tower-1",
            "15th March 2023",
            images = mutableListOf(R.drawable.img_eiffel_tower_1)
        ),
        MockJournalEntry(
            "Eiffel Tower-2",
            "30th August 2023",
            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
            images = mutableListOf(R.drawable.img_eiffel_tower_1, R.drawable.img_eiffel_tower_2, R.drawable.img_eiffel_tower_3)
        ),
        MockJournalEntry(
            "Eiffel Tower-3",
            "1st January 2024",
            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
        )
    )
}
