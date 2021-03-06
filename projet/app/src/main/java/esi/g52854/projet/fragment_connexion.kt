
package esi.g52854.projet

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import esi.g52854.projet.databinding.ActivityMainBinding
import esi.g52854.projet.databinding.FragmentConnexionBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_connexion.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_connexion : Fragment() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentConnexionBinding>(inflater,
            R.layout.fragment_connexion ,container,false)
        binding.connexionButton.setOnClickListener{
            checkEmail(binding.emailEdit.text.toString())
        }
        return binding.root
    }
    private fun checkEmail(editText: String) {
        val test = Patterns.EMAIL_ADDRESS.matcher(editText).matches();
        val message = if(test)  "email valide" else "erreur : email invalide"
        Toast.makeText(getActivity(),message , Toast.LENGTH_SHORT).show();

    }
}