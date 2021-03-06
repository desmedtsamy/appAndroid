
package esi.g52854.projet

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import esi.g52854.projet.databinding.FragmentConnexionBinding

class fragment_connexion : Fragment() {

    private lateinit var binding:  FragmentConnexionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         binding = DataBindingUtil.inflate<FragmentConnexionBinding>(inflater,
            R.layout.fragment_connexion ,container,false)
        binding.connexionButton.setOnClickListener{
            checkEmail()
        }
        return binding.root
    }
    private fun checkEmail() {
        val editText = binding.emailEdit.text.toString()
        val test = Patterns.EMAIL_ADDRESS.matcher(editText).matches();
        val message = if(test)  "email valide" else "erreur : email invalide"
        Toast.makeText(getActivity(),message , Toast.LENGTH_SHORT).show();

    }

}