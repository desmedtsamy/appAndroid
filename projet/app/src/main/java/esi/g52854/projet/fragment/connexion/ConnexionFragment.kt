@file:Suppress("PrivatePropertyName")

package esi.g52854.projet.fragment.connexion



import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import esi.g52854.projet.MainActivity
import esi.g52854.projet.R
import esi.g52854.projet.databinding.FragmentConnexionBinding
import java.io.IOException

class ConnexionFragment : Fragment() {


    private lateinit var viewModel: ConnexionViewModel
    private lateinit var binding: FragmentConnexionBinding
    private lateinit var viewModelFactory: ConnexionViewModelFactory
    private val SignIn = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        
        if(!isOnline()){
            findNavController().navigate(R.id.fragment_offline)
        }
        //si l'utilisateur est déjà connecter
        if ((activity as MainActivity).user != "0"){
            findNavController().navigate(R.id.listFragment)
        }
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_connexion, container, false
        )
        viewModelFactory = ConnexionViewModelFactory(requireActivity(),getString(R.string.default_web_client_id))
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ConnexionViewModel::class.java)

        binding.join.setOnClickListener { signIn() }

        return binding.root
    }
    private fun signIn() {
        val signInIntent = viewModel.mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, SignIn)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SignIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acc = completedTask.getResult(ApiException::class.java)
            Toast.makeText(activity, "Signed In Successfully", Toast.LENGTH_SHORT).show()
            viewModel.firebaseGoogleAuth(acc)

            findNavController().navigate(R.id.listFragment)
        } catch (e: ApiException) {
            Toast.makeText(activity, "Sign In Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isOnline(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }

}