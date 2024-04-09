package ca.uwaterloo.treklogue.data.repository

import android.util.Log
import ca.uwaterloo.treklogue.data.model.JournalEntry
import ca.uwaterloo.treklogue.data.model.Landmark
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

typealias Landmarks = List<Landmark>
typealias LandmarksResponse = Response<Landmarks>
typealias UserLandmarksResponse = Response<Landmarks>
typealias AddLandmarkResponse = Response<Boolean>
typealias AddUserLandmarkResponse = Response<Boolean>
typealias DeleteLandmarkResponse = Response<Boolean>

/**
 * Repository for accessing landmarks.
 */
interface LandmarkRepository {

    /**
     * Returns a flow with the landmarks for the current subscription.
     */
    fun getLandmarkList(): Flow<LandmarksResponse>

    /**
     * Returns a flow with the user landmarks for the current subscription.
     */
    fun getUserLandmarkList(): Flow<UserLandmarksResponse>

    /**
     * Adds a landmark that belongs to the current user using the specified [name], [latitude] and [longitude].
     */
    suspend fun addLandmark(name: String, latitude: Double, longitude: Double): AddLandmarkResponse

    /**
     * Adds a local landmark that belongs to the current user using the specified [name], [latitude] and [longitude].
     */
    suspend fun addUserLandmark(name: String, latitude: Double, longitude: Double): AddUserLandmarkResponse

    /**
     * Deletes a given landmark.
     */
    suspend fun deleteLandmark(id: String): DeleteLandmarkResponse
}

/**
 * Repo implementation used in runtime.
 */
@Singleton
class LandmarkFirebaseRepository @Inject constructor(
    @Named("landmarks") private val landmarksRef: CollectionReference,
    @Named("users") private val usersRef: CollectionReference,
    private val authRepository: AuthRepository,
) : LandmarkRepository {

    override fun getLandmarkList() = callbackFlow {
        val snapshotListener = landmarksRef.orderBy("name").addSnapshotListener { snapshot, e ->
            val landmarksResponse = if (snapshot != null) {
                val landmarks = snapshot.toObjects(Landmark::class.java)
                Response.Success(landmarks)
            } else {
                Response.Failure(e)
            }
            trySend(landmarksResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getUserLandmarkList() = callbackFlow {
        val snapshotListener = usersRef.whereEqualTo("email", authRepository.currentUser?.email)
            .addSnapshotListener { snapshot, e ->
                val userLandmarksResponse = if (snapshot != null) {
                    val users = snapshot.toObjects(User::class.java)
                    Response.Success(users[0].landmarks)
                } else {
                    Response.Failure(e)
                }
                trySend(userLandmarksResponse)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addLandmark(name: String, latitude: Double, longitude: Double) = try {
        val id = landmarksRef.document().id
        val landmark = Landmark(
            id = id,
            name = name,
            latitude = latitude,
            longitude = longitude
        )
        landmarksRef.document(id).set(landmark).await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun addUserLandmark(name: String, latitude: Double, longitude: Double) = try {
        var userId: String? = null
        var userEmail: String? = null
        var userLandmarks: MutableList<Landmark>? = null
        var journalEntries: MutableList<JournalEntry>? = null

        usersRef.whereEqualTo("email", authRepository.currentUser?.email).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val users = it.result.toObjects(User::class.java)
                    userId = it.result.documents[0].id
                    userEmail = users[0].email
                    userLandmarks = users[0].landmarks
                    journalEntries = users[0].journalEntries
                }
            }.await()

        if (userId != null && userEmail != null && userLandmarks != null) {
            val id = landmarksRef.document().id
            val landmark = Landmark(
                id = id,
                name = name,
                latitude = latitude,
                longitude = longitude
            )
            userLandmarks!!.add(landmark)

            if (userLandmarks!!.size == 1) {
                val newUser = User(
                    userId!!,
                    userEmail!!,
                    journalEntries!!,
                    userLandmarks!!
                )
                usersRef.document(userId!!).set(newUser).await()
            } else {
                usersRef.document(userId!!).update("landmarks", userLandmarks).await()
            }
            Log.v(null, "Added landmark successfully.")
        }
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun deleteLandmark(id: String) = try {
        landmarksRef.document(id).delete().await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

}
