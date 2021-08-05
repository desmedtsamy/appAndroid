package esi.g52854.projet.fragment.connection



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import esi.g52854.projet.MainActivity
import esi.g52854.projet.R
import esi.g52854.projet.databinding.FragmentConnectionBinding
import esi.g52854.projet.databinding.FragmentListBinding

class connectionFragment : Fragment() {

    private lateinit var signInButton: SignInButton
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val TAG = "prout"
    private var mAuth: FirebaseAuth? = null
    private var btnSignOut: Button? = null
    private val RC_SIGN_IN = 1
    private lateinit var binding: FragmentConnectionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if ((activity as MainActivity).getUser() != "0"){

            findNavController().navigate(R.id.listFragment)
        }
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_connection, container, false
        )

         signInButton = binding.signInButton
        binding.ViewRecipes.setOnClickListener {

            findNavController().navigate(R.id.listFragment)
        }
        mAuth = FirebaseAuth.getInstance()
         btnSignOut = binding.singout

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

         mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.signInButton.setOnClickListener(View.OnClickListener { signIn() })

        btnSignOut!!.setOnClickListener(View.OnClickListener {

            mGoogleSignInClient.signOut()
            Toast.makeText(requireActivity(), "You are Logged Out", Toast.LENGTH_SHORT).show()
            btnSignOut!!.setVisibility(View.INVISIBLE)
        })

        return binding.root
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acc = completedTask.getResult(ApiException::class.java)
            Toast.makeText(requireActivity(), "Signed In Successfully", Toast.LENGTH_SHORT).show()
            FirebaseGoogleAuth(acc)
        } catch (e: ApiException) {
            Toast.makeText(requireActivity(), "Sign In Failed", Toast.LENGTH_SHORT).show()

            FirebaseGoogleAuth(null)
        }
    }

    private fun FirebaseGoogleAuth(acct: GoogleSignInAccount?) {
        //check if the account is null
        if (acct != null) {
            val authCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
            mAuth!!.signInWithCredential(authCredential).addOnCompleteListener(requireActivity(), OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireActivity(), "Successful", Toast.LENGTH_SHORT).show()
                    val user = mAuth!!.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            })
        } else {
            Toast.makeText(requireActivity(), "acc failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(fUser: FirebaseUser?) {
        var mainActivity: MainActivity = activity as MainActivity
        if (fUser != null) {
            mainActivity.setUser(fUser.uid)
        }
        findNavController().navigate(R.id.listFragment)
    }
}