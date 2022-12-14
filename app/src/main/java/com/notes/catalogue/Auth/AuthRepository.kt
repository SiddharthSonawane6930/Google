package com.notes.catalogue.Auth

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.notes.catalogue.utils.Constants.USERS
import com.notes.catalogue.utils.HelperClass.logErrorMessage

internal class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection(USERS)
    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential?): MutableLiveData<User> {
        val authenticatedUserMutableLiveData: MutableLiveData<User> = MutableLiveData<User>()
        firebaseAuth.signInWithCredential(googleAuthCredential!!)
            .addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    val isNewUser =
                        authTask.result.additionalUserInfo!!.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        val email = firebaseUser.email
                        val user = User(uid, name, email)
                        user.isNew = isNewUser
                        authenticatedUserMutableLiveData.setValue(user)
                    }
                } else {
                    logErrorMessage(authTask.exception!!.message)
                }
            }
        return authenticatedUserMutableLiveData
    }

    fun createUserInFirestoreIfNotExists(authenticatedUser: User): MutableLiveData<User> {
        val newUserMutableLiveData: MutableLiveData<User> = MutableLiveData<User>()
        val uidRef = authenticatedUser.uid?.let { usersRef.document(it) }
        uidRef?.get()?.addOnCompleteListener { uidTask: Task<DocumentSnapshot> ->
            if (uidTask.isSuccessful) {
                val document = uidTask.result
                if (!document.exists()) {
                    uidRef?.set(authenticatedUser)
                        ?.addOnCompleteListener { userCreationTask: Task<Void?> ->
                            if (userCreationTask.isSuccessful) {
                                authenticatedUser.isCreated = true
                                newUserMutableLiveData.setValue(authenticatedUser)
                            } else {
                                logErrorMessage(userCreationTask.exception!!.message)
                            }
                        }
                } else {
                    newUserMutableLiveData.setValue(authenticatedUser)
                }
            } else {
                logErrorMessage(uidTask.exception!!.message)
            }
        }
        return newUserMutableLiveData
    }
}