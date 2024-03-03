package ca.uwaterloo.treklogue.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockJournalEntry

class ListViewModel : ViewModel() {
    private val _selectedJournalEntry = MutableLiveData<MockJournalEntry>()
    val selectedJournalEntry: LiveData<MockJournalEntry> get() = _selectedJournalEntry

    fun selectJournalEntry(journalEntry: MockJournalEntry) {
        _selectedJournalEntry.value = journalEntry
    }

    // Mock: List of journal entries
    val journalEntries = listOf(
        MockJournalEntry(
            "Eiffel Tower-1",
            "15th March 2023",
            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
            R.drawable.img_eiffel_tower
        ),
        MockJournalEntry(
            "Eiffel Tower-2",
            "30th August 2023",
            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
            R.drawable.img_eiffel_tower
        ),
        MockJournalEntry(
            "Eiffel Tower-3",
            "1st January 2024",
            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
            R.drawable.img_eiffel_tower
        )
    )
}
