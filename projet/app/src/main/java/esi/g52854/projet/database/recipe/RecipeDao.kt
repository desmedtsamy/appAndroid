package esi.g52854.projet.database.recipe

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe)

    @Update
    fun update(recipe: Recipe)

    @Query("SELECT * from Recipe WHERE recipeId = :key")
    fun get(key: Long): Recipe?

    @Query("SELECT * from Recipe WHERE titre = :titre")
    fun get(titre: String): Recipe?

    @Query("DELETE FROM Recipe")
    fun clear()

    @Query("SELECT * FROM Recipe ORDER BY recipeId ASC LIMIT 1")
    fun getUser(): Recipe?
    @Query("SELECT * FROM Recipe ORDER BY recipeId ASC")
    fun getAllUser(): LiveData<List<Recipe>>

}