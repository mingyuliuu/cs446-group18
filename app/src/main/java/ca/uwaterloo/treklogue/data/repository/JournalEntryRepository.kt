package ca.uwaterloo.treklogue.data.repository

import android.util.Log
import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.model.User
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
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
        landmarkId: String,
        landmarkName: String,
        visitedAt: String,
        photos: MutableList<String>,
        description: String
    ): AddJournalEntryResponse

    /**
     * Updates a journal entry using new list of [photos] and [description].
     */
    suspend fun updateJournalEntry(
        journalEntryIndex: Int,
        photos: MutableList<String>,
        description: String
    ): UpdateJournalEntryResponse

    /**
     * Deletes a given journal entry.
     */
    suspend fun deleteJournalEntry(journalEntryIndex: Int): DeleteJournalEntryResponse
}

/**
 * Repo implementation used in runtime.
 */
@Singleton
class JournalEntryFirebaseRepository @Inject constructor(
    @Named("users") private val usersRef: CollectionReference,
    private val authRepository: AuthRepository,
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
        landmarkId: String,
        landmarkName: String,
        visitedAt: String,
        photos: MutableList<String>,
        description: String
    ) = try {
        var userId: String? = null
        var userEmail: String? = null
        var journalEntries: MutableList<JournalEntry>? = null

        usersRef.whereEqualTo("email", authRepository.currentUser?.email).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val users = it.result.toObjects(User::class.java)
                    userId = it.result.documents[0].id
                    userEmail = users[0].email
                    journalEntries = users[0].journalEntries
                }
            }.await()

        if (userId != null && userEmail != null && journalEntries != null) {
            val newJournalEntry = JournalEntry(
                if (journalEntries!!.size == 0) 0 else journalEntries!![journalEntries!!.size - 1].index + 1,
                landmarkId,
                landmarkName,
                visitedAt,
                photos,
                description
            )
            journalEntries!!.add(newJournalEntry)

            if (journalEntries!!.size == 1) {
                val newUser = User(
                    userEmail!!,
                    userEmail!!,
                    journalEntries!!
                )
                usersRef.document(userId!!).set(newUser).await()
            } else {
                usersRef.document(userId!!).update("journalEntries", journalEntries).await()
            }
            Log.v(null, "Added journal entry successfully.")
        }
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun updateJournalEntry(
        journalEntryIndex: Int,
        photos: MutableList<String>,
        description: String
    ) = try {
        usersRef.whereEqualTo("email", authRepository.currentUser?.email).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val users = it.result.toObjects(User::class.java)
                    val userId = it.result.documents[0].id
                    val journalEntries = users[0].journalEntries
                    val newJournalEntries = journalEntries.map { entry ->
                        if (entry.index != journalEntryIndex) entry
                        else JournalEntry(
                            entry.index,
                            entry.landmarkId,
                            entry.name,
                            entry.visitedAt,
                            photos,
                            description
                        )
                    }
                    usersRef.document(userId).update("journalEntries", newJournalEntries)
                    Log.v(null, "Updated journal entry successfully.")
                }
            }
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun deleteJournalEntry(journalEntryIndex: Int) = try {
        usersRef.whereEqualTo("email", authRepository.currentUser?.email).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val users = it.result.toObjects(User::class.java)
                    val userId = it.result.documents[0].id
                    val journalEntries = users[0].journalEntries
                    val newJournalEntries = journalEntries.filter { entry ->
                        entry.index != journalEntryIndex
                    }
                    usersRef.document(userId).update("journalEntries", newJournalEntries)
                    Log.v(null, "Deleted journal entry successfully.")
                }
            }
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

}
