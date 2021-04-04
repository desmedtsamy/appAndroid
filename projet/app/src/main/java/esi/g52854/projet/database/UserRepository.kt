package esi.g52854.projet.database

import android.util.Log
import androidx.lifecycle.LiveData

class UserRepository (private val userDao: UserDao){

    val readAllData: LiveData<List<User>> = userDao.getAllUser()
    suspend fun addUser(user: User){
        userDao.insert(user)
    }
    suspend fun updateUser(user: User){

        userDao.update(user)
    }
    suspend fun getUserByEmail(email:String):User?{
    val user = userDao.get(email)
       return user
    }
}