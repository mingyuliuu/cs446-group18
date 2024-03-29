package ca.uwaterloo.treklogue.data.repository

import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.data.model.Landmark
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Named

typealias JournalEntries = List<JournalEntry>
typealias JournalEntriesResponse = Response<JournalEntries>
typealias AddJournalEntryResponse = Response<Boolean>
typealias UpdateJournalEntryResponse = Response<Boolean>
typealias DeleteJournalEntryResponse = Response<Boolean>

/**
 * Repository for accessing journal entries for the current user.
 */
interface JournalEntryRepository {

    /**
     * Returns a flow with the journal entries for the current user.
     */
    fun getJournalEntryList(): Flow<JournalEntriesResponse>

    /**
     * Adds a journal entry that belongs to the current user using the specified [landmark], [visitedAt] date, list of [photos], and [description].
     */
    suspend fun addJournalEntry(
        landmarkId: String,
        visitedAt: String,
        photos: MutableList<String>,
        description: String
    ): AddJournalEntryResponse

    /**
     * Updates a journal entry using new list of [photos] and [description].
     */
    suspend fun updateJournalEntry(
        journalEntryId: String,
        photos: MutableList<String>,
        description: String
    ): UpdateJournalEntryResponse

    /**
     * Deletes a given journal entry.
     */
    suspend fun deleteJournalEntry(journalEntryId: String): DeleteJournalEntryResponse
}

/**
 * Repo implementation used in runtime.
 */
class JournalEntryFirebaseRepository @Inject constructor(
    @Named("users") private val usersRef: CollectionReference,
    private val authRepository: AuthRepository,
    @Named("landmarks") private val landmarksRef: CollectionReference
) : JournalEntryRepository {

    override fun getJournalEntryList() = callbackFlow {
        val snapshotListener = usersRef.document(authRepository.currentUser?.uid!!)
            .addSnapshotListener { snapshot, e ->
                val journalEntriesResponse = if (snapshot != null) {
                    val user = snapshot.toObject(User::class.java)
                    Response.Success(user!!.journalEntries)
                } else {
                    Response.Failure(e)
                }
                trySend(journalEntriesResponse)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addJournalEntry(
        landmarkId: String,
        visitedAt: String,
        photos: MutableList<String>,
        description: String
    ) = try {
        val landmarkRef = landmarksRef.document(landmarkId)
        var landmarkName = "Landmark"
        landmarkRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                landmarkName = it.result.toObject(Landmark::class.java)!!.name
            }
        }

        usersRef.document(authRepository.currentUser?.uid!!).update(
            "journalEntries", FieldValue.arrayUnion(
                JournalEntry(
                    landmarkId = landmarkId,
                    name = landmarkName,
                    visitedAt = visitedAt,
                    photos = photos,
                    description = description
                )
            )
        )
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    // TODO
    override suspend fun updateJournalEntry(
        journalEntryId: String,
        photos: MutableList<String>,
        description: String
    ) = try {
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    // TODO
    override suspend fun deleteJournalEntry(journalEntryId: String) = try {
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

}
