//package ca.uwaterloo.treklogue.data.repository
//
//import android.util.Log
//import ca.uwaterloo.treklogue.app
//import ca.uwaterloo.treklogue.data.model.Badge
//import ca.uwaterloo.treklogue.data.model.Landmark
//import io.realm.kotlin.Realm
//import io.realm.kotlin.ext.query
//import io.realm.kotlin.mongodb.User
//import io.realm.kotlin.mongodb.exceptions.SyncException
//import io.realm.kotlin.mongodb.subscriptions
//import io.realm.kotlin.mongodb.sync.SyncConfiguration
//import io.realm.kotlin.mongodb.sync.SyncSession
//import io.realm.kotlin.mongodb.syncSession
//import io.realm.kotlin.notifications.ResultsChange
//import io.realm.kotlin.query.RealmQuery
//import io.realm.kotlin.query.Sort
//import io.realm.kotlin.types.RealmList
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.launch
//import kotlin.time.Duration.Companion.seconds
//
///**
// * Repository for accessing Realm Sync.
// */
//interface BadgeSyncRepository : BaseSyncRepository {
//
//    /**
//     * Returns a flow with the badges for the current subscription.
//     */
//    fun getBadgeList(): Flow<ResultsChange<Badge>>
//
//    /**
//     * Returns a badge with the given name.
//     */
//    fun getBadgeByName(name: String): RealmQuery<Badge>
//
//    /**
//     * Adds a badge that belongs to the current user using the specified [name] and [landmarks].
//     */
//    suspend fun addBadge(name: String, landmarks: RealmList<Landmark>)
//
//    /**
//     * Deletes a given badge.
//     */
//    suspend fun deleteBadge(badge: Badge)
//}
//
///**
// * Repo implementation used in runtime.
// */
//class BadgeRealmSyncRepository(
//    onSyncError: (session: SyncSession, error: SyncException) -> Unit
//) : BadgeSyncRepository {
//
//    private val realm: Realm
//    private val config: SyncConfiguration
//    private val currentUser: User
//        get() = app.currentUser!!
//
//    init {
//        config = SyncConfiguration.Builder(currentUser, setOf(Badge::class))
//            .initialSubscriptions { realm ->
//                // Subscribe to all badges that belong to the current user
//                add(getAllQuery(realm))
//            }
//            .errorHandler { session: SyncSession, error: SyncException ->
//                onSyncError.invoke(session, error)
//            }
//            .waitForInitialRemoteData()
//            .build()
//
//        realm = Realm.open(config)
//
//        // Mutable states must be updated on the UI thread
//        CoroutineScope(Dispatchers.Main).launch {
//            realm.subscriptions.waitForSynchronization()
//            Log.v(null, "Successfully opened realm: ${realm.configuration} configured for BadgeSyncRepository")
//        }
//    }
//
//    override fun getBadgeList(): Flow<ResultsChange<Badge>> {
//        return realm.query<Badge>()
//            .sort(Pair("_id", Sort.ASCENDING))
//            .asFlow()
//    }
//
//    override fun getBadgeByName(name: String): RealmQuery<Badge> {
//        return realm.query<Badge>("name == $0", name)
//    }
//
//    override suspend fun addBadge(name: String, landmarks: RealmList<Landmark>) {
//        val badge = Badge().apply {
//            this.ownerId = currentUser.id
//            this.name = name
//            this.landmarks = landmarks
//        }
//        realm.write {
//            copyToRealm(badge)
//        }
//    }
//
//    override suspend fun deleteBadge(badge: Badge) {
//        realm.write {
//            delete(findLatest(badge)!!)
//        }
//        realm.subscriptions.waitForSynchronization(10.seconds)
//    }
//
//    override fun pauseSync() {
//        realm.syncSession.pause()
//    }
//
//    override fun resumeSync() {
//        realm.syncSession.resume()
//    }
//
//    override fun close() = realm.close()
//
//    private fun getAllQuery(realm: Realm): RealmQuery<Badge> =
//        realm.query("ownerId == $0", currentUser.id)
//
//}
