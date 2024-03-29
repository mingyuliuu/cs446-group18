package ca.uwaterloo.treklogue.data.repository

import ca.uwaterloo.treklogue.app
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Repository allowing users to create accounts or log in to the app with an existing account.
 */
interface AuthRepository {

    val currentUser: FirebaseUser?

    /**
     * Creates an account with the specified [email] and [password].
     */
    suspend fun createAccount(email: String, password: String)

    /**
     * Logs in with the specified [email] and [password].
     */
    suspend fun login(email: String, password: String)

    fun logout()
}

/**
 * [AuthRepository] for authenticating with MongoDB.
 */
class AuthFirebaseRepository @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun createAccount(email: String, password: String) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(email).build())
            ?.await()
    }

    override suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
        app.login(Credentials.emailPassword(email, password))
    }

    override fun logout() {
        auth.signOut()
    }
}
