package ca.uwaterloo.treklogue.data.repository

import ca.uwaterloo.treklogue.app
import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.data.model.Landmark
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.exceptions.SyncException
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.mongodb.sync.SyncSession
import io.realm.kotlin.mongodb.syncSession
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmQuery
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import kotlin.time.Duration.Companion.seconds

/**
 * Repository for accessing Realm Sync.
 */
interface JournalEntrySyncRepository : BaseSyncRepository {

    /**
     * Returns a flow with the journal entries for the current user.
     */
    fun getJournalEntryList(): Flow<ResultsChange<JournalEntry>>

    /**
     * Adds a journal entry that belongs to the current user using the specified [landmark], [visitedAt] date, list of [photos], and [description].
     */
    suspend fun addJournalEntry(
        landmark: Landmark,
        visitedAt: String,
        photos: RealmList<String>,
        description: String
    )

    /**
     * Updates a journal entry using new list of [photos] and [description].
     */
    suspend fun updateJournalEntry(
        id: ObjectId,
        photos: RealmList<String>,
        description: String
    )

    /**
     * Deletes a given journal entry.
     */
    suspend fun deleteJournalEntry(journalEntry: JournalEntry)
}

/**
 * Repo implementation used in runtime.
 */
class JournalEntryRealmSyncRepository(
    onSyncError: (session: SyncSession, error: SyncException) -> Unit
) : JournalEntrySyncRepository {

    private val realm: Realm
    private val config: SyncConfiguration
    private val currentUser: User
        get() = app.currentUser!!

    init {
        config = SyncConfiguration.Builder(currentUser, setOf(Landmark::class, JournalEntry::class))
            .initialSubscriptions { realm ->
                // Subscribe to all journal entries that belong to the current user
                add(getAllQuery(realm))
            }
            .errorHandler { session: SyncSession, error: SyncException ->
                onSyncError.invoke(session, error)
            }
            .waitForInitialRemoteData()
            .build()

        realm = Realm.open(config)

        // Mutable states must be updated on the UI thread
        CoroutineScope(Dispatchers.Main).launch {
            realm.subscriptions.waitForSynchronization()
        }
    }

    override fun getJournalEntryList(): Flow<ResultsChange<JournalEntry>> {
        return realm.query<JournalEntry>("ownerId == $0", currentUser.id)
            .sort(Pair("_id", Sort.ASCENDING))
            .asFlow()
    }

    override suspend fun addJournalEntry(
        landmark: Landmark,
        visitedAt: String,
        photos: RealmList<String>,
        description: String
    ) {
        val journalEntry = JournalEntry().apply {
            this.ownerId = currentUser.id
            this.landmark = landmark
            this.visitedAt = visitedAt
            this.photos = photos
            this.description = description
        }
        realm.write {
            copyToRealm(journalEntry)
        }
    }

    override suspend fun updateJournalEntry(
        id: ObjectId,
        photos: RealmList<String>,
        description: String
    ) {
        realm.write {
            val journalEntry = query<JournalEntry>("_id == $0", id).find().first()
            journalEntry.photos = photos
            journalEntry.description = description
        }
    }

    override suspend fun deleteJournalEntry(journalEntry: JournalEntry) {
        realm.write {
            delete(findLatest(journalEntry)!!)
        }
        realm.subscriptions.waitForSynchronization(10.seconds)
    }

    override fun pauseSync() {
        realm.syncSession.pause()
    }

    override fun resumeSync() {
        realm.syncSession.resume()
    }

    override fun close() = realm.close()

    private fun getAllQuery(realm: Realm): RealmQuery<JournalEntry> =
        realm.query("ownerId == $0", currentUser.id)

}
