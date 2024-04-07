package ca.uwaterloo.treklogue.util

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class StorageServices {

    companion object {
        suspend fun uploadToStorage(uri: Uri, context: Context): Uri? {
            val result = suspendCoroutine { cont ->
                val storageRef = Firebase.storage.reference
                val uniqueUuid = UUID.randomUUID()
                val spaceRef =
                    storageRef.child("images/$uniqueUuid.jpg")

                val byteArray: ByteArray? =
                    context.contentResolver.openInputStream(uri)?.use { it.readBytes() }

                byteArray?.let {
                    val uploadTask = spaceRef.putBytes(byteArray)
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            Toast.makeText(context, "Upload failed.", Toast.LENGTH_SHORT).show()
                            task.exception?.let {
                                throw it
                            }
                        }
                        spaceRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            Log.v(
                                null,
                                "Image with url [${downloadUri}] uploaded successfully."
                            )
                            cont.resume(task.result)
                        } else {
                            cont.resume(Uri.parse(""))
                        }
                    }
                }
            }

            return result
        }
    }
}