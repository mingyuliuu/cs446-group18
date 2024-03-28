package ca.uwaterloo.treklogue.ui.viewModels

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ca.uwaterloo.treklogue.app
import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.data.model.Landmark
import ca.uwaterloo.treklogue.data.repository.JournalEntryRealmSyncRepository
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Types of UX events triggered by user actions.
 */
sealed class JournalEntryEvent(val message: String) {
    class Redirect(message: String) : JournalEntryEvent(message)
    class ShowMessage(message: String) : JournalEntryEvent(message)

}

class JournalEntryViewModel(
    private val journalEntryRepository: JournalEntryRealmSyncRepository
) : ViewModel() {
    private val _selectedJournalEntry = MutableLiveData<JournalEntry>()
    val selectedJournalEntry: LiveData<JournalEntry> get() = _selectedJournalEntry

    private val _journalEntries = listOf<JournalEntry>()
    val journalEntries: List<JournalEntry> get() = _journalEntries

    private val _event: MutableSharedFlow<JournalEntryEvent> = MutableSharedFlow()
    val event: Flow<JournalEntryEvent>
        get() = _event

    private val currentUser: User
        get() = app.currentUser!!

    fun selectJournalEntry(journalEntry: JournalEntry) {
        _selectedJournalEntry.value = journalEntry
    }

    fun createJournalEntry(landmark: Landmark) {
        val dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val newJournalEntry = JournalEntry(currentUser.id, landmark, dateString)
        _selectedJournalEntry.value = newJournalEntry
    }

    fun saveJournalEntry(
        landmark: Landmark,
        visitedAt: String,
        photos: RealmList<String>,
        description: String
    ) {
        Log.v(null, "Saving journal entry...")
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                // Create a new journal entry
                // TODO: Add logic if saving an existing journal
                Log.v(null, "runCatching...")
                journalEntryRepository.addJournalEntry(landmark, visitedAt, photos, description)
            }.onSuccess {
                _event.emit(
                    JournalEntryEvent.ShowMessage(
                        "Journal entry saved successfully."
                    )
                )
                // TODO: Redirect to previous page
                _event.emit(
                    JournalEntryEvent.Redirect(
                        "Redirecting to the previous page..."
                    )
                )
            }.onFailure { ex: Throwable ->
                _event.emit(JournalEntryEvent.ShowMessage("Failed to save journal entry: $ex"))
            }
        }
    }

    // Mock: List of journal entries
//    val journalEntries = listOf(
//        MockJournalEntry(
//            0,
//            "Eiffel Tower-1",
//            "15th March 2023",
//            images = mutableListOf(R.drawable.img_eiffel_tower_1)
//        ),
//        MockJournalEntry(
//            1,
//            "Eiffel Tower-2",
//            "30th August 2023",
//            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
//            images = mutableListOf(
//                R.drawable.img_eiffel_tower_1,
//                R.drawable.img_eiffel_tower_2,
//                R.drawable.img_eiffel_tower_3
//            )
//        ),
//        MockJournalEntry(
//            2,
//            "Eiffel Tower-3",
//            "1st January 2024",
//            "The Eiffel Tower (/ˈaɪfəl/ EYE-fəl; French: Tour Eiffel [tuʁ ɛfɛl] ⓘ) is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower from 1887 to 1889. ",
//        )
//    )

    companion object {
        fun factory(
            repository: JournalEntryRealmSyncRepository,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null
        ): AbstractSavedStateViewModelFactory {
            return object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return JournalEntryViewModel(repository) as T
                }
            }
        }
    }
}
