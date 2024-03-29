package ca.uwaterloo.treklogue.data.di

import ca.uwaterloo.treklogue.data.repository.AuthFirebaseRepository
import ca.uwaterloo.treklogue.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesAuthRepository(repo: AuthFirebaseRepository): AuthRepository = repo

}