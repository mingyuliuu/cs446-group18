package ca.uwaterloo.treklogue.data.repository

/**
 * Repository for accessing Realm Sync.
 */
interface BaseSyncRepository {

    /**
     * Pauses synchronization with MongoDB. This is used to emulate a scenario of no connectivity.
     */
    fun pauseSync()

    /**
     * Resumes synchronization with MongoDB.
     */
    fun resumeSync()

    /**
     * Closes the realm instance held by this repository.
     */
    fun close()
}
