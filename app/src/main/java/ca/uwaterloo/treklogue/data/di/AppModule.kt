package ca.uwaterloo.treklogue.data.di

import ca.uwaterloo.treklogue.data.repository.AuthFirebaseRepository
import ca.uwaterloo.treklogue.data.repository.AuthRepository
import ca.uwaterloo.treklogue.data.repository.LandmarkFirebaseRepository
import ca.uwaterloo.treklogue.data.repository.LandmarkRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideLandmarkRepository(
        @Named("landmarks") landmarksRef: CollectionReference
    ): LandmarkRepository = LandmarkFirebaseRepository(landmarksRef)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Named("landmarks")
    fun provideLandmarksRef(): CollectionReference = Firebase.firestore.collection("landmarks")

    @Provides
    @Named("users")
    fun provideUsersRef(): CollectionReference = Firebase.firestore.collection("users")

    @Provides
    fun providesAuthRepository(repo: AuthFirebaseRepository): AuthRepository = repo

}