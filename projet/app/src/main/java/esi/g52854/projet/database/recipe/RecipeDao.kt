package esi.g52854.projet.database.recipe

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(perso: Perso)

    @Update
    fun update(perso: Perso)

    @Query("SELECT * from Recipe WHERE recipeId = :key")
    fun get(key: Long): Perso?

    @Query("SELECT * from Recipe WHERE titre = :titre")
    fun get(titre: String): Perso?

    @Query("DELETE FROM Recipe")
    fun clear()

    @Query("SELECT * FROM Recipe ORDER BY recipeId ASC LIMIT 1")
    fun getUser(): Perso?
    @Query("SELECT * FROM Recipe ORDER BY recipeId ASC")
    fun getAllUser(): LiveData<List<Perso>>

}