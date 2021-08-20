package esi.g52854.projet.fragment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import esi.g52854.projet.MainActivity

class DetailViewModelFactory(private val mainActivity: MainActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mainActivity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
