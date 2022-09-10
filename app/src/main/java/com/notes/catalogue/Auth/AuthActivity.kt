@file:Suppress("PackageName")

package com.notes.catalogue.Auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.notes.catalogue.R
import com.notes.catalogue.activities.Home
import com.notes.catalogue.utils.Constants.RC_SIGN_IN
import com.notes.catalogue.utils.Constants.USER
import com.notes.catalogue.utils.HelperClass.logErrorMessage

class  AuthActivity : AppCompatActivity() {
    private var authViewModel: AuthViewModel? = null
    private var googleSignInClient: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        initSignInButton()
        initAuthViewModel()
        initGoogleSignInClient()
    }

    private fun initSignInButton() {
        val googleSignInButton = findViewById<SignInButton>(R.id.google_sign_in_button)
        googleSignInButton.setOnClickListener { v: View? -> signIn() }
    }

    private fun initAuthViewModel() {
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount = task.getResult(
                    ApiException::class.java
                )
                googleSignInAccount?.let { getGoogleAuthCredential(it) }
            } catch (e: ApiException) {
                logErrorMessage(e.message)
            }
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        authViewModel?.signInWithGoogle(googleAuthCredential)
        authViewModel?.authenticatedUserLiveData?.observe(this) { authenticatedUser ->
            if (authenticatedUser.isNew) {
                createNewUser(authenticatedUser)
            } else {
                goToMainActivity(authenticatedUser)
            }
        }
    }

    private fun createNewUser(authenticatedUser: User) {
        authViewModel?.createUser(authenticatedUser)
        authViewModel?.createdUserLiveData?.observe(this) { user ->
            if (user.isCreated) {
                user.name?.let { toastMessage(it) }
            }
            goToMainActivity(user)
        }
    }

    private fun toastMessage(name: String) {
        Toast.makeText(this, "Hi $name!\nYour account was successfully created.", Toast.LENGTH_LONG)
            .show()
    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this@AuthActivity, Home::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
        finish()
    }
}