package esi.g52854.projet.database.recipe

import androidx.lifecycle.LiveData

class RecipeRepository (private val recipeDao: RecipeDao){

    val readAllData: LiveData<List<Recipe>> = recipeDao.getAllUser()
    suspend fun addRecipe(recipe: Recipe){
        recipeDao.insert(recipe)
    }
    suspend fun updateRecipe(recipe: Recipe){

        recipeDao.update(recipe)
    }
    suspend fun getRecipeBytitle(title:String): Recipe?{
    val user = recipeDao.get(title)
       return user
    }
}