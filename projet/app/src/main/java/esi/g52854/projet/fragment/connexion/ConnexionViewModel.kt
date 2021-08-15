package esi.g52854.projet.fragment.connexion

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import esi.g52854.projet.MainActivity

class ConnexionViewModel (private var activity : Activity, web_client_id : String) : ViewModel(){

      var mGoogleSignInClient: GoogleSignInClient
    private var mAuth: FirebaseAuth? = null


    init{
        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(web_client_id)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }


     fun firebaseGoogleAuth(acct: GoogleSignInAccount?) {
        //check if the account is null
        if (acct != null) {
            val authCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
            mAuth!!.signInWithCredential(authCredential).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = mAuth!!.currentUser
                    val mainActivity: MainActivity = activity as MainActivity
                    mainActivity.user = user!!.uid
                }
            }
        }
    }
}