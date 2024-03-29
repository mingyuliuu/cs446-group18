package ca.uwaterloo.treklogue.data.repository

import ca.uwaterloo.treklogue.app
import ca.uwaterloo.treklogue.data.model.Response
import ca.uwaterloo.treklogue.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.CollectionReference
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

typealias CreateAccountResponse = Response<Boolean>

/**
 * Repository allowing users to create accounts or log in to the app with an existing account.
 */
interface AuthRepository {

    val currentUser: FirebaseUser?

    /**
     * Creates an account with the specified [email] and [password].
     */
    suspend fun createAccount(email: String, password: String): CreateAccountResponse

    /**
     * Logs in with the specified [email] and [password].
     */
    suspend fun login(email: String, password: String)

    fun logout()
}

/**
 * [AuthRepository] for authenticating with Firebase Auth.
 */
class AuthFirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    @Named("users") private val usersRef: CollectionReference
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun createAccount(email: String, password: String) = try {
        // Create account
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(email).build())
            ?.await()

        // Add user to db
        val id = usersRef.document().id
        val user = User(
            id = email, // use email as the primary id
            email = email
        )
        usersRef.document(id).set(user).await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
        app.login(Credentials.emailPassword(email, password))
    }

    override fun logout() {
        auth.signOut()
    }
}
