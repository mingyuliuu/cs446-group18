package ca.uwaterloo.treklogue.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.uwaterloo.treklogue.R
import ca.uwaterloo.treklogue.data.mockModel.MockJournalEntry
import ca.uwaterloo.treklogue.data.mockModel.MockLandmark
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.repository.AddJournalEntryResponse
import ca.uwaterloo.treklogue.data.repository.DeleteJournalEntryResponse
import ca.uwaterloo.treklogue.data.repository.JournalEntriesResponse
import ca.uwaterloo.treklogue.data.repository.JournalEntryRepository
import ca.uwaterloo.treklogue.data.repository.UpdateJournalEntryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class JournalEntryViewModel @Inject constructor(
    private val journalEntryRepository: JournalEntryRepository
) : ViewModel() {
    private val _selectedJournalEntry = MutableLiveData<MockJournalEntry>()
    val selectedJournalEntry: LiveData<MockJournalEntry> get() = _selectedJournalEntry

    var journalEntryResponse by mutableStateOf<JournalEntriesResponse>(Response.Loading)
        private set
    var addJournalEntryResponse by mutableStateOf<AddJournalEntryResponse>(Response.Success(false))
        private set
    var updateJournalEntryResponse by mutableStateOf<UpdateJournalEntryResponse>(
        Response.Success(
            false
        )
    )
        private set
    var deleteJournalEntryResponse by mutableStateOf<DeleteJournalEntryResponse>(
        Response.Success(
            false
        )
    )
        private set

    init {
        getJournalEntries()
    }

    fun selectJournalEntry(journalEntry: MockJournalEntry) {
        _selectedJournalEntry.value = journalEntry
    }

    fun createJournalEntry(landmark: MockLandmark) {
        val dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val newIndex = journalEntries.size
        val newJournalEntry = MockJournalEntry(newIndex, landmark.name, dateString)
        _selectedJournalEntry.value = newJournalEntry
    }

    // Mock: List of journal entries
    val journalEntries = listOf(
        MockJournalEntry(
            0,
            "Eiffel Tower-1",
            "15th March 2023",
            images = mutableListOf(R.drawable.img_eiffel_tower_1)
        ),
        MockJournalEntry(
            1,
            "Eiffel Tower-2",
            "30th August 2023",
            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
            images = mutableListOf(
                R.drawable.img_eiffel_tower_1,
                R.drawable.img_eiffel_tower_2,
                R.drawable.img_eiffel_tower_3
            )
        ),
        MockJournalEntry(
            2,
            "Eiffel Tower-3",
            "1st January 2024",
            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
        )
    )

    private fun getJournalEntries() = viewModelScope.launch {
        journalEntryRepository.getJournalEntryList().collect { response ->
            journalEntryResponse = response
        }
    }

    // TODO: Implement the following three
    fun addJournalEntry(
        landmarkId: String,
        photos: MutableList<String>,
        description: String
    ) = viewModelScope.launch {
        addJournalEntryResponse = Response.Loading

        val dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        addJournalEntryResponse = journalEntryRepository.addJournalEntry(
            landmarkId,
            dateString,
            photos,
            description
        )
    }

    fun updateJournalEntry(
        journalEntryId: String,
        photos: MutableList<String>,
        description: String
    ) = viewModelScope.launch {
        addJournalEntryResponse = Response.Loading
        addJournalEntryResponse = journalEntryRepository.updateJournalEntry(
            journalEntryId,
            photos,
            description,
        )
    }

    fun deleteJournalEntry(
        journalEntryId: String
    ) = viewModelScope.launch {
        addJournalEntryResponse = Response.Loading
        addJournalEntryResponse = journalEntryRepository.deleteJournalEntry(
            journalEntryId
        )
    }

}
