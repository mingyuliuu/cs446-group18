//package ca.uwaterloo.treklogue.data.repository
//
//import ca.uwaterloo.treklogue.app
//import ca.uwaterloo.treklogue.data.model.Badge
//import ca.uwaterloo.treklogue.data.model.JournalEntry
//import ca.uwaterloo.treklogue.data.model.Landmark
//import io.realm.kotlin.Realm
//import io.realm.kotlin.ext.query
//import io.realm.kotlin.ext.realmListOf
//import io.realm.kotlin.mongodb.User
//import io.realm.kotlin.mongodb.exceptions.SyncException
//import io.realm.kotlin.mongodb.subscriptions
//import io.realm.kotlin.mongodb.sync.SyncConfiguration
//import io.realm.kotlin.mongodb.sync.SyncSession
//import io.realm.kotlin.mongodb.syncSession
//import io.realm.kotlin.query.RealmQuery
//import io.realm.kotlin.types.RealmList
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlin.time.Duration.Companion.seconds
//import ca.uwaterloo.treklogue.data.model.User as AppUser
//
///**
// * Repository for accessing Realm Sync.
// */
//interface UserSyncRepository : BaseSyncRepository {
//
//    /**
//     * Adds a user with all corresponding information.
//     */
//    suspend fun addUser(
//        name: String = "",
//        email: String = "",
//        avatar: String = "",
//        journalEntries: RealmList<JournalEntry> = realmListOf(),
//        badges: RealmList<Badge> = realmListOf()
//    )
//
//    /**
//     * Updates a user using new [name], [avatar], [journalEntries] and [badges].
//     */
//    suspend fun updateUser(
//        name: String,
//        avatar: String,
//        journalEntries: RealmList<JournalEntry>,
//        badges: RealmList<Badge>
//    )
//
//    /**
//     * Deletes a given user.
//     */
//    suspend fun deleteUser(user: AppUser)
//}
//
///**
// * Repo implementation used in runtime.
// */
//class UserRealmSyncRepository(
//    onSyncError: (session: SyncSession, error: SyncException) -> Unit
//) : UserSyncRepository {
//
//    private val realm: Realm
//    private val config: SyncConfiguration
//    private val currentUser: User
//        get() = app.currentUser!!
//
//    init {
//        config = SyncConfiguration.Builder(
//            currentUser,
//            setOf(JournalEntry::class, Badge::class, AppUser::class)
//        )
//            .initialSubscriptions { realm ->
//                add(getCurrentUser(realm))
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
//        }
//    }
//
//    override suspend fun addUser(
//        name: String,
//        email: String,
//        avatar: String,
//        journalEntries: RealmList<JournalEntry>,
//        badges: RealmList<Badge>
//    ) {
//        val user = AppUser().apply {
//            this.name = name
//            this.email = email
//            this.avatar = avatar
//            this.journalEntries = journalEntries
//            this.badges = badges
//        }
//        realm.write {
//            copyToRealm(user)
//        }
//    }
//
//    override suspend fun updateUser(
//        name: String,
//        avatar: String,
//        journalEntries: RealmList<JournalEntry>,
//        badges: RealmList<Badge>
//    ) {
//        realm.write {
//            val user = query<AppUser>("_id == $0", currentUser.id).find().first()
//            user.name = name
//            user.avatar = avatar
//            user.journalEntries = journalEntries
//            user.badges = badges
//        }
//    }
//
//    override suspend fun deleteUser(user: AppUser) {
//        realm.write {
//            delete(findLatest(user)!!)
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
//    private fun getCurrentUser(realm: Realm): RealmQuery<AppUser> =
//        realm.query("_id == $0", currentUser.id)
//
//}
