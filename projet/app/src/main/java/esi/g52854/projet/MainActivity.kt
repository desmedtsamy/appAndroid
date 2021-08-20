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

    private var _user: String = "0"
    var recette : Recette? = null
    var user: String
        get() = _user

        set(newVal){
            Log.i("test42", "main : $newVal")
            _user = newVal

            val preferences: SharedPreferences =
                    applicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("user", user)
            editor.apply()
        }
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout

        val preferences: SharedPreferences = applicationContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        user = preferences.getString("user", 0.toString()).toString()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_drawer, menu)

        return true
    }

}