package com.notes.catalogue.Auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthCredential

class AuthViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val authRepository: AuthRepository
    var authenticatedUserLiveData: LiveData<User>? = null
    var createdUserLiveData: LiveData<User>? = null
    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential)
    }

    fun createUser(authenticatedUser: User?) {
        createdUserLiveData = authenticatedUser?.let {
            authRepository.createUserInFirestoreIfNotExists(
                it
            )
        }
    }

    init {
        authRepository = AuthRepository()
    }
}