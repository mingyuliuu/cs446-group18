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

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideLandmarksRef() = Firebase.firestore.collection("landmarks")

    @Provides
    fun provideLandmarkRepository(
        landmarksRef: CollectionReference
    ): LandmarkRepository = LandmarkFirebaseRepository(landmarksRef)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesAuthRepository(repo: AuthFirebaseRepository): AuthRepository = repo

}