package esi.g52854.projet

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import esi.g52854.projet.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var user:String
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout

        val preferences: SharedPreferences = applicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        user = preferences.getString("user", 0.toString()).toString()

        Log.i("prout","user = "+ user)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_drawer, menu)

        return true
    }
    fun setUser (newVal:String){
        user = newVal
       Log.i("gnee","set user :"+user)

        val preferences: SharedPreferences =
            applicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("user", user)
        editor.commit()
    }
    fun getUser(): String {
        Log.i("gnee","get user : "+user)
        return user
    }

}