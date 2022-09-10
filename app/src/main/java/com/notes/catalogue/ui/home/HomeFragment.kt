package com.notes.catalogue.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.notes.catalogue.R
import com.notes.catalogue.databinding.FragmentHomeBinding
import com.notes.catalogue.utils.Constants.USER

class HomeFragment : Fragment(),FirebaseAuth.AuthStateListener {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null
    private var messageTextView: TextView? = null
    private var _binding: FragmentHomeBinding?=null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val user: String = USER
        initGoogleSignInClient()
        initMessageTextView()
        setMessageToMessageTextView(user)

        setHasOptionsMenu(true);
        return binding.root
    }
//Here is the code
//        private val userFromIntent: User?
//        private get() = intent.getSerializableExtra(Constants.USER) as User?

        private fun initGoogleSignInClient() {
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build()
            googleSignInClient = context?.let { GoogleSignIn.getClient(it, googleSignInOptions) }
        }

        private fun initMessageTextView() {
            messageTextView = binding.messageTextView
        }

        private fun setMessageToMessageTextView(user: String) {
            val message = "You are logged in as: $user"
            messageTextView!!.text = message
        }

        override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                goToAuthInActivity()
            }
        }

        private fun goToAuthInActivity() {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    //        val intent = Intent(this, AuthActivity::class.java)
    //        startActivity(intent)
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

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            return inflater.inflate(R.menu.main_menu, menu)
        }

        override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.sign_out_button) {
                signOut()
                return true
            }
            return super.onOptionsItemSelected(menuItem)
        }
}