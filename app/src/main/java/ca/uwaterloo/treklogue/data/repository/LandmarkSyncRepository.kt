package ca.uwaterloo.treklogue.data.repository

import android.util.Log
import ca.uwaterloo.treklogue.app
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
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * Repository for accessing Realm Sync.
 */
interface LandmarkSyncRepository : BaseSyncRepository {

    /**
     * Returns a flow with the landmarks for the current subscription.
     */
    fun getLandmarkList(): Flow<ResultsChange<Landmark>>

    /**
     * Adds a landmark that belongs to the current user using the specified [name], [latitude] and [longitude].
     */
    suspend fun addLandmark(name: String, latitude: Double, longitude: Double)

    /**
     * Deletes a given landmark.
     */
    suspend fun deleteLandmark(landmark: Landmark)
}

/**
 * Repo implementation used in runtime.
 */
class LandmarkRealmSyncRepository(
    onSyncError: (session: SyncSession, error: SyncException) -> Unit
) : LandmarkSyncRepository {

    private val realm: Realm
    private val config: SyncConfiguration
    private val currentUser: User
        get() = app.currentUser!!

    init {
        config = SyncConfiguration.Builder(currentUser, setOf(Landmark::class))
            .initialSubscriptions { realm ->
                // Subscribe to all landmarks
                add(getAllQuery(realm))
            }
            .errorHandler { session: SyncSession, error: SyncException ->
                onSyncError.invoke(session, error)
            }
            .waitForInitialRemoteData()
            .build()

        realm = Realm.open(config)

        // Can write to database like this:
        /* CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                realm.write {
                    val newLandmark = Landmark().apply {
                        name = "Niagara Falls"
                        latitude = 43.107565
                        longitude = -79.060331
                    }
                    copyToRealm(newLandmark)
                }
            }.onSuccess {
                Log.v(null, "Successfully added landmark.")
            }.onFailure { ex: Throwable ->
                Log.v(null, "Failed to add landmark: " + ex.message)
            }
        }
         */

        // Mutable states must be updated on the UI thread
        CoroutineScope(Dispatchers.Main).launch {
            realm.subscriptions.waitForSynchronization()
            Log.v(null, "Successfully opened realm: ${realm.configuration}")
        }
    }

    override fun getLandmarkList(): Flow<ResultsChange<Landmark>> {
        return realm.query<Landmark>()
            .sort(Pair("_id", Sort.ASCENDING))
            .asFlow()
    }

    override suspend fun addLandmark(name: String, latitude: Double, longitude: Double) {
        val landmark = Landmark().apply {
            this.name = name
            this.latitude = latitude
            this.longitude = longitude
        }
        realm.write {
            copyToRealm(landmark)
        }
    }

    override suspend fun deleteLandmark(landmark: Landmark) {
        realm.write {
            delete(findLatest(landmark)!!)
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

    private fun getAllQuery(realm: Realm): RealmQuery<Landmark> =
        realm.query<Landmark>()

}
