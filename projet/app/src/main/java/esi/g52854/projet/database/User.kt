package esi.g52854.projet.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    @ColumnInfo(name = "last_Connection")
    val last_Connection: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "email")
    var email: String
)
