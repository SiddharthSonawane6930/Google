package com.notes.catalogue.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.notes.catalogue.Auth.AuthViewModel
import com.notes.catalogue.Auth.User
import com.notes.catalogue.R
import com.notes.catalogue.activities.MainActivity
import com.notes.catalogue.databinding.FragmentLoginBinding
import com.notes.catalogue.utils.Constants.RC_SIGN_IN
import com.notes.catalogue.utils.HelperClass.logErrorMessage


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var authViewModel: AuthViewModel? = null
    private var googleSignInClient: GoogleSignInClient? = null
    private var MainActivity: MainActivity?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        initSignInButton()
        initAuthViewModel()
        initGoogleSignInClient()
        MainActivity()
        return binding.root

    }

//    Here is the thing
private fun initSignInButton() {
    val googleSignInButton = binding.googleSignInButton
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
        googleSignInClient = context?.let { GoogleSignIn.getClient(it, googleSignInOptions) }
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
                user.name?.let { MainActivity?.toastMessage(it) }
            }
            goToMainActivity(user)
        }
    }



    private fun goToMainActivity(user: User) {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}