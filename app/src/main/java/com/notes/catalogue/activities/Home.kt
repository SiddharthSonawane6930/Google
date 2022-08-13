package com.notes.catalogue.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.notes.catalogue.Auth.AuthActivity
import com.notes.catalogue.Auth.User
import com.notes.catalogue.R
import com.notes.catalogue.utils.Constants.USER

class Home : AppCompatActivity(), AuthStateListener {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null
    private var messageTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val user: User? = userFromIntent
        initGoogleSignInClient()
        initMessageTextView()
        setMessageToMessageTextView(user)
    }

    private val userFromIntent: User?
        private get() = intent.getSerializableExtra(USER) as User?

    private fun initGoogleSignInClient() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun initMessageTextView() {
        messageTextView = findViewById(R.id.message_text_view)
    }

    private fun setMessageToMessageTextView(user: User?) {
        val message = "You are logged in as: " + (user?.name)
        messageTextView!!.text = message
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            goToAuthInActivity()
        }
    }

    private fun goToAuthInActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }

    private fun signOut() {
        singOutFirebase()
        signOutGoogle()
    }

    private fun singOutFirebase() {
        firebaseAuth.signOut()
    }

    private fun signOutGoogle() {
        googleSignInClient!!.signOut()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.sign_out_button) {
            signOut()
            return true
        }
        return super.onOptionsItemSelected(menuItem)
    }
}