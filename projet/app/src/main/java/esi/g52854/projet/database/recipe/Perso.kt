package esi.g52854.projet.database.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipe")
data class Perso(
    @PrimaryKey(autoGenerate = true)
    var recipeId: Long = 0L,

    @ColumnInfo(name = "titre")
    val titre: String,
    @ColumnInfo(name = "prepaDuration")
    val prepaDuration: Int,
    @ColumnInfo(name = "Duration")
    val duration: Int,
    @ColumnInfo(name = "difficulty")
    val difficulty: Int,
    @ColumnInfo(name = "people")
    val people: String,
    @ColumnInfo(name = "steps")
    val steps: String,
    @ColumnInfo(name = "ingredients")
    val ingredients: String
)
