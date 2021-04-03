package esi.g52854.projet.connexion

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import esi.g52854.projet.R

class ConnexionViewModel : ViewModel() {

     fun checkEmail(editText: String ):Boolean{
        val validEmail = Patterns.EMAIL_ADDRESS.matcher(editText).matches()
        return validEmail

    }
}