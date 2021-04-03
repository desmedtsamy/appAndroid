package esi.g52854.projet.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * from User WHERE userId = :key")
    fun get(key: Long): User?

    @Query("SELECT * from User WHERE email = :email")
    fun get(email: String): User?

    @Query("DELETE FROM User")
    fun clear()

    @Query("SELECT * FROM User ORDER BY userId ASC LIMIT 1")
    fun getUser(): User?
    @Query("SELECT * FROM User ORDER BY userId ASC")
    fun getAllUser(): LiveData<List<User>>

}