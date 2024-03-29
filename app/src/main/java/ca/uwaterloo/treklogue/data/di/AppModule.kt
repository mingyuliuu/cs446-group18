package ca.uwaterloo.treklogue.data.di

import ca.uwaterloo.treklogue.data.repository.AuthFirebaseRepository
import ca.uwaterloo.treklogue.data.repository.AuthRepository
import ca.uwaterloo.treklogue.data.repository.JournalEntryFirebaseRepository
import ca.uwaterloo.treklogue.data.repository.JournalEntryRepository
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
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    @Named("landmarks")
    fun provideLandmarksRef(): CollectionReference = Firebase.firestore.collection("landmarks")

    @Provides
    @Singleton
    @Named("users")
    fun provideUsersRef(): CollectionReference = Firebase.firestore.collection("users")

    @Provides
    @Singleton
    fun providesAuthRepository(repo: AuthFirebaseRepository): AuthRepository = repo

    @Provides
    @Singleton
    fun provideLandmarkRepository(
        @Named("landmarks") landmarksRef: CollectionReference
    ): LandmarkRepository = LandmarkFirebaseRepository(landmarksRef)

    @Provides
    @Singleton
    fun provideJournalEntryRepository(
        @Named("users") usersRef: CollectionReference,
        authRepository: AuthRepository,
        @Named("landmarks") landmarksRef: CollectionReference
    ): JournalEntryRepository =
        JournalEntryFirebaseRepository(usersRef, authRepository, landmarksRef)

}