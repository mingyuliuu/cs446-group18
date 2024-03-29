package ca.uwaterloo.treklogue.data.repository

import ca.uwaterloo.treklogue.data.model.Landmark
import ca.uwaterloo.treklogue.data.model.Response
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

typealias Landmarks = List<Landmark>
typealias LandmarksResponse = Response<Landmarks>
typealias AddLandmarkResponse = Response<Boolean>
typealias DeleteLandmarkResponse = Response<Boolean>

/**
 * Repository for accessing Realm Sync.
 */
interface LandmarkRepository {

    /**
     * Returns a flow with the landmarks for the current subscription.
     */
    fun getLandmarkList(): Flow<LandmarksResponse>

    /**
     * Adds a landmark that belongs to the current user using the specified [name], [latitude] and [longitude].
     */
    suspend fun addLandmark(name: String, latitude: Double, longitude: Double): AddLandmarkResponse

    /**
     * Deletes a given landmark.
     */
    suspend fun deleteLandmark(id: String): DeleteLandmarkResponse
}

/**
 * Repo implementation used in runtime.
 */
class LandmarkFirebaseRepository @Inject constructor(private val landmarksRef: CollectionReference) : LandmarkRepository {

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

    override suspend fun deleteLandmark(id: String) = try {
        landmarksRef.document(id).delete().await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

}
