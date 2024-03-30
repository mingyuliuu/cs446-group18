package ca.uwaterloo.treklogue.data.repository

import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

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
     * Adds a journal entry that belongs to the current user using the specified [landmarkName], [visitedAt] date, list of [photos], and [description].
     */
    suspend fun addJournalEntry(
        landmarkName: String,
        visitedAt: String,
        photos: MutableList<String>,
        description: String
    ): AddJournalEntryResponse

    /**
     * Updates a journal entry using new list of [photos] and [description].
     */
    suspend fun updateJournalEntry(
        landmarkName: String,
        photos: MutableList<String>,
        description: String
    ): UpdateJournalEntryResponse

    /**
     * Deletes a given journal entry.
     */
    suspend fun deleteJournalEntry(landmarkName: String): DeleteJournalEntryResponse
}

/**
 * Repo implementation used in runtime.
 */
@Singleton
class JournalEntryFirebaseRepository @Inject constructor(
    @Named("users") private val usersRef: CollectionReference,
    private val authRepository: AuthRepository,
    @Named("landmarks") private val landmarksRef: CollectionReference
) : JournalEntryRepository {

    override fun getJournalEntryList() = callbackFlow {
        val snapshotListener = usersRef.whereEqualTo("email", authRepository.currentUser?.email)
            .addSnapshotListener { snapshot, e ->
                val journalEntriesResponse = if (snapshot != null) {
                    val users = snapshot.toObjects(User::class.java)
                    Response.Success(users[0].journalEntries)
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
        landmarkName: String,
        visitedAt: String,
        photos: MutableList<String>,
        description: String
    ) = try {
        val newJournalEntry = JournalEntry(
            landmarkName,
            visitedAt,
            photos,
            description
        )

        usersRef.whereEqualTo("email", authRepository.currentUser?.email).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val users = it.result.toObjects(User::class.java)
                if (users[0].id != null) {
                    usersRef.document(users[0].id!!).update("journalEntries", FieldValue.arrayUnion(newJournalEntry))
                }

            }
        }

        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    // TODO: Implement this
    override suspend fun updateJournalEntry(
        landmarkName: String,
        photos: MutableList<String>,
        description: String
    ) = try {
//        usersRef.whereEqualTo("email", authRepository.currentUser?.email).get().addOnCompleteListener {
//            if (it.isSuccessful) {
//                val users = it.result.toObjects(User::class.java)
//                if (users[0].id != null) {
//                    usersRef.document(users[0].id!!).update("journalEntries", FieldValue.arrayUnion(newJournalEntry))
//                }
//
//            }
//        }
//        usersRef.document(authRepository.currentUser?.uid!!).update(
//            "journalEntries", FieldValue.arrayUnion(
//                JournalEntry(
//                    landmarkId = landmarkId,
//                    name = landmarkName,
//                    visitedAt = visitedAt,
//                    photos = photos,
//                    description = description
//                )
//            )
//        )
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    // TODO: Implement this
    override suspend fun deleteJournalEntry(landmarkName: String) = try {
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

}
