package ca.uwaterloo.treklogue.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.data.model.Landmark
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.repository.AddJournalEntryResponse
import ca.uwaterloo.treklogue.data.repository.DeleteJournalEntryResponse
import ca.uwaterloo.treklogue.data.repository.JournalEntriesResponse
import ca.uwaterloo.treklogue.data.repository.JournalEntryRepository
import ca.uwaterloo.treklogue.data.repository.UpdateJournalEntryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class JournalEntryState(
    val selectedJournalEntry: JournalEntry = JournalEntry()
) {
    companion object {
        val initialState =
            JournalEntryState()
    }
}

@HiltViewModel
class JournalEntryViewModel @Inject constructor(
    private val journalEntryRepository: JournalEntryRepository
) : ViewModel() {
    private val _state: MutableStateFlow<JournalEntryState> =
        MutableStateFlow(JournalEntryState.initialState)
    val state: StateFlow<JournalEntryState>
        get() = _state.asStateFlow()

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

    fun selectJournalEntry(journalEntry: JournalEntry) {
        _state.value = state.value.copy(selectedJournalEntry = journalEntry)
    }

    fun createJournalEntry(landmark: Landmark) {
        val dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val newJournalEntry = JournalEntry(0, landmark.id, landmark.name, dateString)
        _state.value = state.value.copy(selectedJournalEntry = newJournalEntry)
    }

    private fun getJournalEntries() = viewModelScope.launch {
        journalEntryRepository.getJournalEntryList().collect { response ->
            journalEntryResponse = response
        }
    }

    fun addJournalEntry(
        landmarkId: String,
        landmarkName: String,
        photos: MutableList<String>,
        description: String,
        rating: Float,
    ) = viewModelScope.launch {
        addJournalEntryResponse = Response.Loading

        val dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        addJournalEntryResponse = journalEntryRepository.addJournalEntry(
            landmarkId,
            landmarkName,
            dateString,
            photos,
            description,
            rating,
        )
    }

    fun updateJournalEntry(
        journalEntryIndex: Int,
        photos: MutableList<String>,
        description: String,
        rating: Float,
    ) = viewModelScope.launch {
        addJournalEntryResponse = Response.Loading
        addJournalEntryResponse = journalEntryRepository.updateJournalEntry(
            journalEntryIndex,
            photos,
            description,
            rating,
        )
    }

    fun deleteJournalEntry(
        journalEntryIndex: Int
    ) = viewModelScope.launch {
        addJournalEntryResponse = Response.Loading
        addJournalEntryResponse = journalEntryRepository.deleteJournalEntry(
            journalEntryIndex
        )
    }

}
