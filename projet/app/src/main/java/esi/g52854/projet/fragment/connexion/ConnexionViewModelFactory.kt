package esi.g52854.projet.fragment.connexion

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ConnexionViewModelFactory (private var activity : Activity, private var web_client_id : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConnexionViewModel::class.java)) return ConnexionViewModel(activity,web_client_id) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}