package esi.g52854.projet.database.recipe

import androidx.lifecycle.LiveData

class RecipeRepository (private val recipeDao: RecipeDao){

    val readAllData: LiveData<List<Perso>> = recipeDao.getAllUser()
    suspend fun addRecipe(perso: Perso){
        recipeDao.insert(perso)
    }
    suspend fun updateRecipe(perso: Perso){

        recipeDao.update(perso)
    }
    suspend fun getRecipeBytitle(title:String): Perso?{
    val user = recipeDao.get(title)
       return user
    }
}