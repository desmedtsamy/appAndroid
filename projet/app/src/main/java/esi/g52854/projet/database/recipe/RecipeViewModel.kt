package esi.g52854.projet.database.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Perso>>
    private val repository: RecipeRepository

    init {
        val recipeDao = RecipeDatabase.getInstance(application).recipeDao
        repository = RecipeRepository(recipeDao)
        readAllData = repository.readAllData
    }

    fun addRecipe(perso: Perso){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRecipe(perso)
        }
    }
    fun updateRecipe(perso: Perso){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRecipe(perso)
        }
    }
    fun getRecipeBytitle(email:String){
        var recipe = Perso(0,"",0,0,0,"","","")
        viewModelScope.launch(Dispatchers.IO) {
            recipe = repository.getRecipeBytitle(email)!!

        }

    }


}