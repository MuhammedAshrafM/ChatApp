package com.example.chatapp.data.repositories

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.chatapp.data.network.ExceptionsMapper
import com.example.chatapp.domain.base.DataState
import com.example.chatapp.domain.model.User
import com.example.chatapp.utils.Constants.USERS_DOCUMENT_ID
import com.example.chatapp.utils.getBytesFromUriImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class AddNewUserInfoRepositoryImpl @Inject constructor(
    private val exceptionsMapper: ExceptionsMapper,
    private val context: Context,
    private val firesStorage: FirebaseStorage,
    private val fireStore: FirebaseFirestore
) {
    fun uploadProfileImage(
        uri: Uri?,
        path: String,
        onUploadListener: (DataState<Uri?>) -> Unit
    ) {
        val bytes = getBytesFromUriImage(context, uri)
        val storageRef = firesStorage.reference.child(path)

        if (bytes == null)
            onUploadListener(
                DataState.error(
                    DataState.HttpError(
                        "Failed to upload the image Please try again!",
                        null
                    )
                )
            )
        else {
            val uploadTask = storageRef.putBytes(bytes)

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageRef.downloadUrl
            }.addOnCompleteListener { task ->
                onUploadListener(
                    task.run {
                        if (isSuccessful) {
                            val downloadUri = task.result
                            DataState.success(downloadUri)
                        } else {
                            DataState.error(
                                DataState.HttpError(
                                    null,
                                    exceptionsMapper.mapFromException(exception)
                                )
                            )
                        }
                    }
                )

            }

        }
    }

    fun addNewUserInfo(user: User, onAddedListener: (DataState<Unit>) -> Unit) {
        fireStore.collection(USERS_DOCUMENT_ID)
            .document(user.uid!!)
            .set(user)
            .addOnSuccessListener {
                onAddedListener(DataState.success(Unit))
            }
            .addOnFailureListener { e ->
                onAddedListener(
                    DataState.error(
                        DataState.HttpError(
                            null,
                            exceptionsMapper.mapFromException(e)
                        )
                    )
                )
            }
    }


}