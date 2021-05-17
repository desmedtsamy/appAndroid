package esi.g52854.projet.database.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Recipe>>
    private val repository: RecipeRepository

    init {
        val recipeDao = RecipeDatabase.getInstance(application).recipeDao
        repository = RecipeRepository(recipeDao)
        readAllData = repository.readAllData
    }

    fun addRecipe(recipe: Recipe){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addRecipe(recipe)
        }
    }
    fun updateRecipe(recipe: Recipe){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRecipe(recipe)
        }
    }
    fun getRecipeBytitle(email:String){
        var recipe = Recipe(0,"",0,0,0,"","","")
        viewModelScope.launch(Dispatchers.IO) {
            recipe = repository.getRecipeBytitle(email)!!

        }

    }


}