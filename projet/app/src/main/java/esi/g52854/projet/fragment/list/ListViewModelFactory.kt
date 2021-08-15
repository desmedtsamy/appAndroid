package esi.g52854.projet.fragment.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import esi.g52854.projet.MainActivity

@Suppress("UNCHECKED_CAST")
class ListViewModelFactory (private val user: String, private val days: Array<String>, private val main: MainActivity, private val navController : NavController) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(user,days,main,navController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}