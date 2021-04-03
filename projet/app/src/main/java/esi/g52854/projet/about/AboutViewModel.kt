package esi.g52854.projet.about

import android.util.Log
import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {
    init {
        Log.i("About", "AboutViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("About", "AboutViewModel destroyed!")
    }
}